# Triple 사전 과제 *ClubMileage*
--------------------

## 실행환경
### - Gradle
### - JDK 17
### - MySQL 5.7.33
### - SpringBoot 2.7.1
### - Spring Data JPA, lombok, jUnit
---------------------

## 개발환경 및 툴
### - InteliJ IDEA Community Edition 2021.3.1
### - MySQL WorkBench
### - PostMan
### - AWS LightSail
---------------------

## DDL
### Database : `ClubMileage`
* Spring Data JPA를 사용하여 DDL을 직접 입력하지 않아도 DB connection 설정 시 자동으로 테이블이 생성됩니다.
* 현재 AWS LightSail에 데이터베이스가 생성되어있습니다. 그대로 사용하셔도 무방합니다.
* ERD Diagram

![image](https://user-images.githubusercontent.com/71342315/178139243-47f33a08-4fea-4e71-9a0c-14c3db0adb5a.png)


#### Table `attached_photo`
```
CREATE TABLE `attached_photo` (
`attached_photo_ids` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
`review_id` binary(16) DEFAULT NULL,
PRIMARY KEY (`attached_photo_ids`),
KEY `FKjb1h16xhjextj0vgf99cbu22r` (`review_id`),
CONSTRAINT `FKjb1h16xhjextj0vgf99cbu22r` FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```

#### Table `review`
```
CREATE TABLE `review` (
`review_id` binary(16) NOT NULL,
`content` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
`create_date` datetime(6) DEFAULT NULL,
`lastupdated_date` datetime(6) DEFAULT NULL,
`place_id` binary(16) DEFAULT NULL,
`user_id` binary(16) DEFAULT NULL,
PRIMARY KEY (`review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```

#### Table `point_history`
```
CREATE TABLE `point_history` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`action` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
`action_detail` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
`create_date` datetime(6) DEFAULT NULL,
`point_variation` int(11) DEFAULT NULL,
`review_id` binary(16) DEFAULT NULL,
`user_id` binary(16) DEFAULT NULL,
PRIMARY KEY (`id`),
KEY `i_reviewId` (`review_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```

#### Table `personal_point`
```
CREATE TABLE `personal_point` (
`user_id` binary(16) NOT NULL,
`point` int(11) DEFAULT NULL,
PRIMARY KEY (`user_id`),
KEY `i_userId` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```

---------------------------
## API
### * POST EVENTS API("/events")
  - 리뷰 추가, 수정, 삭제
#### Request
```
{
  "type": "REVIEW",
  "action": "ADD", /* "MOD", "DELETE" */
  "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
  "content": "좋아요!",
  "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
  "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
  "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
```
#### Response
```
  "Review Add Success"
```
---
### * GET Index("/index")
  - 리뷰 전체 조회
#### Request
```
HTTP GET (http://localhost:8080/index)
```
#### Response
```
  {
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "content": "좋아요!",
        "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
        "attachedPhotoIds": [
            {
                "attachedPhotoIds": "afb0cef2-851d-4a50-bb07-9cc15cbdc332"
            },
            {
                "attachedPhotoIds": "e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"
            }
        ],
        "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
        "createDate": "2022-07-10T16:17:52.991171",
        "lastupdatedDate": "2022-07-10T16:17:52.991171"
    }
```
---
### * GET Personal_Point API("/mileage/{userId}")
  - 개인 포인트 조회
#### Request
```
HTTP GET (http://localhost:8080/mileage/3ede0ef2-92b7-4817-a5f3-0c575361f745)
```
#### Response
```
  3
```
---------------------------
## 테스트케이스
* 테스트 케이스는 Junit5 를 사용하여 작성하였습니다.
* 모든 테이블가 비워진 상태에서 테스트가 가능합니다.
