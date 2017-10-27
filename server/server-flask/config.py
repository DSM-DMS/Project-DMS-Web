import os
from datetime import timedelta

PORT = 3000

API_VER = '0.1'
API_TITLE = 'DMS'
API_DESC = '''
[ENDPOINT] http://dsm2015.cafe24.com:{0}

- Status Code 401 UNAUTHORIZED는 JWT 토큰이 만료되었음을 뜻합니다.
- Status Code 403 Forbidden은 권한이 없음을 뜻합니다.
- Status Code 500 Internal Server Error는 서버 내부 오류입니다.
'''.format(PORT)

SECRET_KEY = os.getenv('SECRET_KEY')
JWT_SECRET_KEY = SECRET_KEY
JWT_ACCESS_TOKEN_EXPIRES = timedelta(days=365)
JWT_HEADER_TYPE = 'JWT'
# http://flask-jwt-extended.readthedocs.io/en/latest/options.html

MYSQL_PW = os.getenv('MYSQL_PW')
DB_NAME = 'dms'
