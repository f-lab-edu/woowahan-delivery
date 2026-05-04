export function createApi() {
  const API_BASE_URL = import.meta.env.VITE_API_BASE_URL?.replace(/\/$/, '') || '';
  const BASE_URL = `${API_BASE_URL}/api/v1`;

  let isRefreshing = false;
  let refreshSubscribers = []; // 대기 큐 추가

  // refresh 완료 후 대기 중인 요청들 일괄 재시도
  function onRefreshed(newToken) {
    refreshSubscribers.forEach(cb => cb(newToken));
    refreshSubscribers = [];
  }

  // refresh 실패 시 대기 중인 요청들 에러 처리
  function onRefreshFailed(err) {
    refreshSubscribers.forEach(cb => cb(null, err));
    refreshSubscribers = [];
  }

  async function refreshTokens() {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) throw new Error('No refresh token');

    const res = await fetch(`${BASE_URL}/users/auth/refresh`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ refreshToken })
    });

    if (!res.ok) throw new Error('Refresh failed');
    const json = await res.json();

    if (json?.data?.accessToken) {
      localStorage.setItem('accessToken', json.data.accessToken);
      // refreshToken도 갱신 (서버가 rotate한다면 필수)
      if (json.data.refreshToken) {
        localStorage.setItem('refreshToken', json.data.refreshToken);
      }
      return json.data.accessToken;
    }
    throw new Error('Invalid refresh response');
  }

  async function fetchApi(endpoint, options = {}, isRetry = false) {
    const token = localStorage.getItem('accessToken');
    const config = {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        ...options.headers,
      },
    };

    const response = await fetch(`${BASE_URL}${endpoint}`, config);

    if (!response.ok) {
      const isAuthEndpoint = ['/users/auth/login', '/users/auth/refresh'].includes(endpoint);

      // 401이고 retry가 아닐 때만 refresh 시도
      if (response.status === 401 && !isRetry && token && !isAuthEndpoint) {
        
        // 이미 refresh 중이면 → 큐에 넣고 대기
        if (isRefreshing) {
          return new Promise((resolve, reject) => {
            refreshSubscribers.push(async (newToken, err) => {
              if (err) return reject(err);
              resolve(fetchApi(endpoint, options, true)); // 새 토큰으로 재시도
            });
          });
        }

        // refresh 시작
        isRefreshing = true;
        try {
          const newToken = await refreshTokens();
          isRefreshing = false;
          onRefreshed(newToken);               // 대기 중인 요청들 재시도
          return fetchApi(endpoint, options, true); // 현재 요청 재시도
        } catch (refreshErr) {
          isRefreshing = false;
          onRefreshFailed(refreshErr);         // 대기 중인 요청들 에러 처리
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
          window.location.reload();
          throw refreshErr;
        }
      }

      let errMsg = `API Error: ${response.status}`;
      try {
        const errBody = await response.json();
        if (errBody?.message) errMsg = errBody.message;
      } catch (_) {}
      throw new Error(errMsg);
    }

    return response.json();
  }

  let getCartPromise = null;

  return {
    refreshTokens,
    getStores: async (categoryId, cursor = null, size = 15) => {
      let url = `/stores?size=${size}`;
      if (categoryId !== 1) url += `&categoryId=${categoryId}`;
      if (cursor) url += `&cursor=${cursor}`;
      return fetchApi(url);
    },
    getMenus: async (storeId) => {
      return fetchApi(`/stores/${storeId}/menus`);
    },
    createOrder: async (orderData) => {
      return fetchApi(`/orders/order-create`, {
        method: 'POST',
        body: JSON.stringify(orderData)
      });
    },
    getCart: async () => {
      if (getCartPromise) return getCartPromise;
      getCartPromise = fetchApi(`/cart`).finally(() => { getCartPromise = null; });
      return getCartPromise;
    },
    addCartItem: async (menuId, quantity) => {
      return fetchApi(`/cart/items`, {
        method: 'POST',
        body: JSON.stringify({ menuId, quantity })
      });
    },
    updateCartItem: async (cartItemId, quantityChange) => {
      return fetchApi(`/cart/items/${cartItemId}`, {
        method: 'PATCH',
        body: JSON.stringify({ quantity: quantityChange })
      });
    },
    removeCartItem: async (cartItemId) => {
      return fetchApi(`/cart/items/${cartItemId}`, {
        method: 'DELETE'
      });
    },
    signup: async (signupData) => {
      return fetchApi(`/users/auth/signup`, {
        method: 'POST',
        body: JSON.stringify(signupData)
      });
    },
    login: async (email, password) => {
      const res = await fetchApi(`/users/auth/login`, {
        method: 'POST',
        body: JSON.stringify({ email, password })
      });
      if (res.data && res.data.accessToken) {
        localStorage.setItem('accessToken', res.data.accessToken);
        if (res.data.refreshToken) localStorage.setItem('refreshToken', res.data.refreshToken);
      }
      return res;
    },
    logout: () => {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
    },
    isAuthenticated: () => {
      return !!localStorage.getItem('accessToken');
    },
    getKakaoLoginUrl: async () => {
      return fetchApi('/users/oauth/kakao/login');
    },
    kakaoCallback: async (code) => {
      const res = await fetchApi(`/users/oauth/kakao/callback?code=${code}`);
      if (res && res.accessToken) {
        localStorage.setItem('accessToken', res.accessToken);
        if (res.refreshToken) localStorage.setItem('refreshToken', res.refreshToken);
      }
      return res;
    }
  };
}

export const api = createApi();
