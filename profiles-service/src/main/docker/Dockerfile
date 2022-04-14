FROM amazoncorretto:11 as builder

WORKDIR /app/

COPY app ./
RUN java -Djarmode=layertools -jar ${project.build.finalName}.jar extract

FROM amazoncorretto:11

LABEL org.opencontainers.image.authors="Victor Fisyuk <victor.fisyuk@gmail.com>"

WORKDIR /app/

COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
CMD java org.springframework.boot.loader.JarLauncher
