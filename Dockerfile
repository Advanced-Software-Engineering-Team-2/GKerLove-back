FROM openjdk:17
COPY target/*.jar app.jar
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo "Asia/Shanghai" > /etc/timezone
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Shanghai", "-Dspring.profiles.active=prd", "app.jar"]