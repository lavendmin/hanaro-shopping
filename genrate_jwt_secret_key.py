import os
import base64
import hashlib

# SHA256 방식으로 암호화된 JWT 키 생성
def generate_jwt_secret():
    secret = base64.urlsafe_b64encode(os.urandom(32))
    return secret

jwt_secret = generate_jwt_secret()
print("JWT 암호화 키 (HMAC SHA-256):", jwt_secret.decode())
