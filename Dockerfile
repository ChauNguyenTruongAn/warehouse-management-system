# Stage 1: Build dự án
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Sao chép các file cấu hình maven và mã nguồn
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src

# Cấp quyền thực thi cho mvnw và build dự án (bỏ qua unit test để nhanh hơn)
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Stage 2: Tạo image để chạy (Runtime)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Sao chép file jar đã build từ stage 1 vào stage này
COPY --from=build /app/target/*.jar app.jar

# Khai báo biến môi trường mặc định (có thể ghi đè khi chạy container)
ENV DB_URL=jdbc:mysql://localhost:3306/cdnsg_db
ENV DB_USERNAME=root
ENV DB_PASSWORD=root
ENV CORS_ORIGINS=http://localhost:5173

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]