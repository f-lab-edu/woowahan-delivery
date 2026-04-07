package com.dorkem.food.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.dorkem.food.common.annotation.AuthStoreId;
import com.dorkem.food.common.exception.CommonException;
import com.dorkem.food.common.exception.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthStoreIdResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AuthStoreId.class)
			&& parameter.getParameterType().equals(Long.class);
	}

	@Override
	public Object resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory
	) {
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		Long storeId = (Long)request.getAttribute("storeId");

		if (storeId == null) {
			throw new CommonException(ErrorCode.NOT_FOUND_STORE_IN_TOKEN);
		}
		return storeId;
	}
}
