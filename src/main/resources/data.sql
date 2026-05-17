-- 1. 카테고리 데이터
INSERT INTO category (category_id, category_name, slug, sort_order)
VALUES (1, '홈', 'home', 1);
INSERT INTO category (category_id, category_name, slug, sort_order)
VALUES (2, '한식', 'korean', 2);
INSERT INTO category (category_id, category_name, slug, sort_order)
VALUES (3, '분식', 'snack', 3);
INSERT INTO category (category_id, category_name, slug, sort_order)
VALUES (4, '카페·디저트', 'cafe', 4);
INSERT INTO category (category_id, category_name, slug, sort_order)
VALUES (5, '일식', 'japanese', 5);
INSERT INTO category (category_id, category_name, slug, sort_order)
VALUES (6, '치킨', 'chicken', 6);
INSERT INTO category (category_id, category_name, slug, sort_order)
VALUES (7, '피자', 'pizza', 7);
INSERT INTO category (category_id, category_name, slug, sort_order)
VALUES (8, '양식', 'western', 8);
INSERT INTO category (category_id, category_name, slug, sort_order)
VALUES (9, '중식', 'chinese', 9);
INSERT INTO category (category_id, category_name, slug, sort_order)
VALUES (10, '족발·보쌈', 'pork', 10);

-- 2. 유저 데이터 (modified_at 추가)
INSERT INTO users (user_id, username, phone_number, email, password, role, user_profile, created_at, modified_at)
VALUES (1, '김사장', '010-1111-1111', 'owner1@test.com', 'pass123', 'OWNER',
        'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/default_user.png',
        NOW(), NOW());

-- 3. 사장님 데이터
INSERT INTO owners (owner_id, user_id, owner_name, business_number, created_at, modified_at)
VALUES (1, 1, '김사장', '123-45-67890', NOW(), NOW());

-- 4. 식당 데이터
-- [치킨]
INSERT INTO stores (store_id, owner_id, category_id, thumbnail, store_name, business_number, store_address, store_address_details,
                    status, min_order_amount, base_delivery_fee, created_at, modified_at)
VALUES (1, 1, 6, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', 'BBQ 강남점', '123-45-67890', '서울시 강남구', '1층', 'OPEN', 15000, 3000, NOW(), NOW()),
       (2, 1, 6, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '교촌치킨 압구정점', '123-45-67891', '서울시 강남구', '102호', 'OPEN', 16000, 3000, NOW(), NOW()),
       (3, 1, 6, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', 'BHC 청담점', '123-45-67892', '서울시 강남구', '2층', 'OPEN', 15000, 2000, NOW(), NOW());

-- [일식]
INSERT INTO stores (store_id, owner_id, category_id, thumbnail, store_name, business_number, store_address, store_address_details,
                    status, min_order_amount, base_delivery_fee, created_at, modified_at)
VALUES (4, 1, 5, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '상무초밥 송파점', '123-45-67893', '서울시 송파구', '101호', 'OPEN', 20000, 4000, NOW(), NOW()),
       (5, 1, 5, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '홍대돈부리 신촌점', '123-45-67894', '서울시 서대문구', '1층', 'OPEN', 12000, 2500, NOW(), NOW());

-- [한식]
INSERT INTO stores (store_id, owner_id, category_id, thumbnail, store_name, business_number, store_address, store_address_details,
                    status, min_order_amount, base_delivery_fee, created_at, modified_at)
VALUES (6, 1, 2, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '김밥천국 강남본점', '123-45-67895', '서울시 강남구', '1층', 'OPEN', 10000, 2000, NOW(), NOW()),
       (7, 1, 2, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '큰맘할매순대국 역삼점', '123-45-67896', '서울시 강남구', '지하 1층', 'OPEN', 14000, 3000, NOW(), NOW()),
       (8, 1, 2, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '본죽 서초점', '123-45-67897', '서울시 서초구', '1층', 'OPEN', 12000, 3500, NOW(), NOW());

-- [분식]
INSERT INTO stores (store_id, owner_id, category_id, thumbnail, store_name, business_number, store_address, store_address_details,
                    status, min_order_amount, base_delivery_fee, created_at, modified_at)
VALUES (9, 1, 3, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '동대문엽기떡볶이 논현점', '123-45-67898', '서울시 강남구', '2층', 'OPEN', 14000, 3000, NOW(), NOW()),
       (10, 1, 3, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '신전떡볶이 삼성점', '123-45-67899', '서울시 강남구', '1층', 'OPEN', 13000, 2500, NOW(), NOW());

-- [카페·디저트]
INSERT INTO stores (store_id, owner_id, category_id, thumbnail, store_name, business_number, store_address, store_address_details,
                    status, min_order_amount, base_delivery_fee, created_at, modified_at)
VALUES (11, 1, 4, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '메가MGC커피 선릉점', '123-45-67900', '서울시 강남구', '1층', 'OPEN', 8000, 2000, NOW(), NOW()),
       (12, 1, 4, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '스타벅스 강남역점', '123-45-67901', '서울시 강남구', '1, 2층', 'OPEN', 15000, 3000, NOW(), NOW());

-- [피자]
INSERT INTO stores (store_id, owner_id, category_id, thumbnail, store_name, business_number, store_address, store_address_details,
                    status, min_order_amount, base_delivery_fee, created_at, modified_at)
VALUES (13, 1, 7, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '도미노피자 반포점', '123-45-67902', '서울시 서초구', '1층', 'OPEN', 20000, 0, NOW(), NOW()),
       (14, 1, 7, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '파파존스 서초점', '123-45-67903', '서울시 서초구', '1층', 'OPEN', 18000, 2000, NOW(), NOW());

-- [양식]
INSERT INTO stores (store_id, owner_id, category_id, thumbnail, store_name, business_number, store_address, store_address_details,
                    status, min_order_amount, base_delivery_fee, created_at, modified_at)
VALUES (15, 1, 8, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '아웃백스테이크하우스 강남점', '123-45-67904', '서울시 강남구', '3층', 'OPEN', 30000, 5000, NOW(), NOW()),
       (16, 1, 8, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '서가앤쿡 홍대점', '123-45-67905', '서울시 마포구', '2층', 'OPEN', 20000, 4000, NOW(), NOW());

-- [중식]
INSERT INTO stores (store_id, owner_id, category_id, thumbnail, store_name, business_number, store_address, store_address_details,
                    status, min_order_amount, base_delivery_fee, created_at, modified_at)
VALUES (17, 1, 9, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '홍콩반점0410 신사점', '123-45-67906', '서울시 강남구', '지하 1층', 'OPEN', 13000, 2500, NOW(), NOW()),
       (18, 1, 9, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '백종원의 짜장면', '123-45-67907', '서울시 종로구', '1층', 'OPEN', 12000, 2000, NOW(), NOW());

-- [족발·보쌈]
INSERT INTO stores (store_id, owner_id, category_id, thumbnail, store_name, business_number, store_address, store_address_details,
                    status, min_order_amount, base_delivery_fee, created_at, modified_at)
VALUES (19, 1, 10, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '가장맛있는족발 건대점', '123-45-67908', '서울시 광진구', '1층', 'OPEN', 25000, 3000, NOW(), NOW()),
       (20, 1, 10, 'https://woowahan-d.s3.ap-northeast-2.amazonaws.com/defaults/defult_store.png', '원할머니보쌈 종로점', '123-45-67909', '서울시 종로구', '2층', 'OPEN', 30000, 3000, NOW(), NOW());

-- PK 시퀀스
ALTER TABLE users
    ALTER COLUMN user_id RESTART WITH 2;
ALTER TABLE owners
    ALTER COLUMN owner_id RESTART WITH 2;
ALTER TABLE stores
    ALTER COLUMN store_id RESTART WITH 21;
