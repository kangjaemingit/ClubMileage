#ClubMileage
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
#### * Spring Data JPA를 사용하여 DDL을 직접 입력하지 않아도 DB connection 설정 시 자동으로 테이블이 생성됩니다.

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
