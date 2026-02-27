FROM alpine:edge
LABEL maintainer="DER-2SH-KA"
WORKDIR /app
COPY build/application .
RUN chmod +x ./application
ENTRYPOINT ["./application"]
