# 1번째 스테이지 : 빌드 영역
# jdk(java development kit) = 자바 개발 도구 = 용량 좀 큼
FROM eclipse-temurin:17-jdk-alpine AS sprint-build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# 2번째 스테이지 : 실행 영역
# jre(java runtime environment) = 자바 실행환경만 만들어줌 = jdk 보다 빠져있는게 많음(용량 아끼기)
# alpine = 너무 비어있어서 타임존마저 없을 떄도 있어서 타임존을 add 해준 것(로그 시간이 꼬일 수 있음.)
FROM eclipse-temurin:17-jre-alpine AS sprint-exec
COPY --from=sprint-build /app/build/libs/*.jar app.jar
ENV TZ=Asia/Seoul
RUN apk add --no-cache curl tzdata
ENTRYPOINT ["java", "-jar", "app.jar"]