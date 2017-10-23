from flask_jwt import current_identity, jwt_required
from flask_restful_swagger_2 import Resource, request, swagger

from db.models.account import StudentModel

from . import after_signup_doc


class ChangePW(Resource):
    @swagger.doc(after_signup_doc.CHANGE_PW)
    @jwt_required()
    def post(self):
        current_pw = request.form.get('current_pw')
        new_pw = request.form.get('new_pw')

        student = StudentModel.objects(id=current_identity, pw=current_pw).first()
        if not student:
            # Forbidden
            return '', 403
        else:
            student.update(pw=new_pw)

            return '', 201


class ChangeNumber(Resource):
    @swagger.doc(after_signup_doc.CHANGE_NUMBER)
    @jwt_required()
    def post(self):
        new_number = request.form.get('new_number')

        StudentModel.objects(id=current_identity).first().update(number=new_number)

        return '', 201
