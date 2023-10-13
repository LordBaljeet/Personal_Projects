import base64
from django.contrib.auth import get_user_model
from django.contrib.auth.models import Group, User
from django.db.models import Q, Subquery
from .models import *
import ServerSide.securityHandler as server_encryption
import datetime
import django.contrib.auth
import ServerSide.logger as logger
from . import signatures,securityHandler
from django.contrib.auth.models import User
from django.shortcuts import get_object_or_404
from secure_delete import secure_delete
from django.conf import settings

def registerPatient(user, birth_date):
    """
    Registers a patient.

    :param user: The user object representing the patient.
    :type user: User
    :param birth_date: The birth date of the patient.
    :type birth_date: datetime.date
    """
    user_group, created = Group.objects.get_or_create(name='Patient')
    user.groups.add(user_group)
    # Register Patient
    newPatient = Patient(user = user,birth_date=birth_date )
    newPatient.save()

def getUsers():
    """
    Retrieves all users.

    :return: All users.
    :rtype: QuerySet
    """
    return get_user_model().objects.all()

def getUser(username):
    """
    Retrieves a user by username.

    :param username: The username of the user to retrieve.
    :type username: str
    :return: The user object.
    :rtype: User
    """
    return get_user_model().objects.get(username=username)

def deleteUser(user, request):
    """
    Deletes a user.

    :param user: The user to delete.
    :type user: User
    """
    logger.logUserDeletion(user, request.user)
    secure_delete.secure_random_seed_init()
    secure_delete.secure_delete(f'{settings.KEYS_DIRECTORY}/{user.username}_public_key.pem')
    secure_delete.secure_delete(f'{settings.KEYS_DIRECTORY}/{user.username}_private_key.pem')
    user.delete()

def getPatient(user):
    """
    Retrieves the patient associated with a user.

    :param user: The user object.
    :type user: User
    :return: The patient object.
    :rtype: Patient
    """
    return Patient.objects.get(user=user)

def getDoctor(user):
    """
    Retrieves the doctor associated with a user.

    :param user: The user object.
    :type user: User
    :return: The doctor object.
    :rtype: Doctor
    """
    return Doctor.objects.get(user=user)

def getAdmin(user):
    """
    Retrieves the admin associated with a user.

    :param user: The user object.
    :type user: User
    :return: The admin object.
    :rtype: Admin
    """
    return Admin.objects.get(user=user)

def isInAdminWaitingList(user):
    """
    Checks if a user is in the admin waiting list.

    :param user: The user object.
    :type user: User
    :return: True if the user is in the waiting list, False otherwise.
    :rtype: bool
    """
    return AdminWaitingList.objects.filter(waitingAdmin=user).exists()

def isInDoctorWaitingList(user):
    """
    Checks if a user is in the doctor waiting list.

    :param user: The user object.
    :type user: User
    :return: True if the user is in the waiting list, False otherwise.
    :rtype: bool
    """
    return DoctorWaitingList.objects.filter(waitingDoctor=user).exists()

def getDoctorsWaitingList():
    """
    Retrieves all doctors in the waiting list.

    :return: All doctors in the waiting list.
    :rtype: QuerySet
    """
    return DoctorWaitingList.objects.all()

def getAdminsWaitingList():
    """
    Retrieves all admins in the waiting list.

    :return: All admins in the waiting list.
    :rtype: QuerySet
    """
    return AdminWaitingList.objects.all()

def addToAdminWaitingList(user, birth_day):
    """
    Adds a user to the admin waiting list.

    :param user: The user object.
    :type user: User
    :param birth_day: The birth date of the user.
    :type birth_day: datetime.date
    """
    newAdmin = AdminWaitingList(waitingAdmin=user, birth_date=birth_day)
    newAdmin.save()
    logger.logUserWaitingApproval(user)

def approveAdmin(user, admin):
    """
    Approves an admin and adds them to the admin group.

    :param admin: The waiting admin object.
    :type admin: AdminWaitingList
    """
    futurAdmin = admin.waitingAdmin
    user_group, created = Group.objects.get_or_create(name='Admin')
    futurAdmin.groups.add(user_group)
    newAdmin = Admin(user=futurAdmin, birth_date=admin.birth_date)
    newAdmin.save()
    logger.logUserApproval(user,futurAdmin)
    admin.delete()

def declineAdmin(admin):
    """
    Declines an admin and deletes the associated user.

    :param admin: The admin object.
    :type admin: AdminWaitingList
    """
    user = admin.waitingAdmin
    user.delete()

def addToDoctorWaitingList(user, inami):
    """
    Adds a user to the doctor waiting list.

    :param user: The user object.
    :type user: User
    :param inami: The INAMI number of the user.
    :type inami: str
    """
    newDoctor = DoctorWaitingList(waitingDoctor=user, inami=inami)
    newDoctor.save()
    logger.logUserWaitingApproval(user)

def approveDoctor(doctor, request):
    """
    Approves a doctor and adds them to the doctor group.

    :param doctor: The doctor object.
    :type doctor: DoctorWaitingList
    """
    user = doctor.waitingDoctor
    user_group, created = Group.objects.get_or_create(name='Doctor')
    DoctorWaitingList.objects.get(waitingDoctor=user).delete()
    user.groups.add(user_group)
    newDoctor = Doctor(user=user, inami=doctor.inami)
    newDoctor.save()
    logger.logUserApproval(user, request.user)

def declineDoctor(doctor):
    """
    Declines a doctor and deletes the associated user.

    :param doctor: The doctor object.
    :type doctor: DoctorWaitingList
    """
    user = doctor.waitingDoctor
    secure_delete.secure_random_seed_init()
    secure_delete.secure_delete(f'{settings.KEYS_DIRECTORY}/{user.username}_public_key.pem')
    secure_delete.secure_delete(f'{settings.KEYS_DIRECTORY}/{user.username}_private_key.pem')
    user.delete()

def getPatientsAppointedDoctors(patient):
    """
    Retrieves all doctors appointed to a patient.

    :param patient: The patient object.
    :type patient: Patient
    :return: All doctors appointed to the patient.
    :rtype: QuerySet
    """
    return Appointed.objects.filter(patient=patient)
def getPatientId(patientName):
    patient_id = Patient.objects.select_related('user').filter(user__username=patientName).values_list('id', flat=True).first()
    return patient_id
def getAdminId(adminName):
    admin_id = Admin.objects.select_related('user').filter(user__username=adminName).values_list('id', flat=True).first()
    return admin_id

def getDoctorId(doctorName):
    doctor_id = Doctor.objects.select_related('user').filter(user__username=doctorName).values_list('id', flat=True).first()
    return doctor_id
def get_doctors_names(doctors):
    doctor_usernames = Doctor.objects.select_related('user').filter(id__in=doctors).values_list('user__username', flat=True)
    return list(doctor_usernames)
def get_Doctors_User_id(doctors):
    doctor_ids = Doctor.objects.select_related('user').filter(id__in=doctors).values_list('user__id', flat=True)
    return doctor_ids
def get_Patients_User_id(patients):
    patients_ids = Patient.objects.select_related('user').filter(id__in=patients).values_list('user__id', flat=True)
    return patients_ids
def get_Patient_names(patients):
    patient_usernames = Patient.objects.select_related('user').filter(id__in=patients).values_list('user__username', flat=True)
    return list(patient_usernames)

def get_Admin_names(admins):
    admin_usernames = Admin.objects.select_related('user').filter(id__in=admins).values_list('user__username', flat=True)
    return list(admin_usernames)
def getUserId(username):
    userId = User.objects.filter(username = username).values('id').first()
    return userId.get('id')
def get_User_ids(users):
    admin_usernames = Admin.objects.select_related('user').filter(id__in=users).values_list('user__id', flat=True)
    return list(admin_usernames)
def getPatientsNotAppointedDoctors(patient):
    """
    Retrieves all doctors not appointed to a patient.

    :param patient: The patient object.
    :type patient: Patient
    :return: All doctors not appointed to the patient.
    :rtype: QuerySet
    """
    appointed_doctors = Appointed.objects.filter(patient_id=getPatientId(patient)).values_list('doctor_id', flat=True)
    appointed_doctors = get_Doctors_User_id(appointed_doctors)
    unappointed_doctors = Doctor.objects.exclude(user__in=appointed_doctors)
    return unappointed_doctors

def getDoctorsAppointedPatients(doctor):
    """
    Retrieves all patients appointed to a doctor.

    :param doctor: The doctor object.
    :type doctor: Doctor
    :return: All patients appointed to the doctor.
    :rtype: QuerySet
    """
    return Appointed.objects.filter(doctor_id=getDoctorId(doctor))

def getDoctorsNotAppointedPatients(doctor):
    """
    Retrieves all patients not appointed to a doctor.

    :param doctor: The doctor object.
    :type doctor: Doctor
    :return: All patients not appointed to the doctor.
    :rtype: QuerySet
    """
    DoctorsPatients = Appointed.objects.filter(doctor_id=getDoctorId(doctor)).values_list('patient_id', flat=True)
    appointed_doctors = get_Patients_User_id(DoctorsPatients)
    return Patient.objects.exclude(user__in=appointed_doctors)

def getPatientAppointmentRequests(patient):
    """
    Retrieves all appointment requests for a patient.

    :param patient: The patient object.
    :type patient: Patient
    :return: All appointment requests for the patient.
    :rtype: QuerySet
    """
    return AppointmentRequest.objects.filter(patient_id=getPatientId(patient))

def getDoctorAppointmentRequests(doctor):
    """
    Retrieves all appointment requests for a doctor.

    :param doctor: The doctor object.
    :type doctor: Doctor
    :return: All appointment requests for the doctor.
    :rtype: QuerySet
    """
    return AppointmentRequest.objects.filter(doctor_id=getDoctorId(doctor))

def appointDoctor(patient, doctor):
    """
    Appoints a doctor to a patient.

    :param patient: The patient object.
    :type patient: Patient
    :param doctor: The doctor object.
    :type doctor: Doctor
    """
    
    newAppointment = Appointed(patient=patient, doctor=doctor)
    newAppointment.save()
    logger.logAppointmentCreated(newAppointment)

def removeAppointment(patient, doctor):
    """
    Removes an appointment between a patient and a doctor.

    :param patient: The patient object.
    :type patient: Patient
    :param doctor: The doctor object.
    :type doctor: Doctor
    """
    appointment = Appointed.objects.get(patient=patient, doctor=doctor)
    logger.logAppointmentDeleted(appointment)
    appointment.delete()

def approveAppointment(patient, doctor):
    """
    Approves an appointment request and appoints the doctor to the patient.

    :param patient: The patient object.
    :type patient: Patient
    :param doctor: The doctor object.
    :type doctor: Doctor
    """
    appointDoctor(patient, doctor)
    appointmentRequest = AppointmentRequest.objects.get(patient=patient, doctor=doctor)
    appointmentRequest.delete()

def declineAppointment(patient, doctor):
    """
    Declines an appointment request.

    :param patient: The patient object.
    :type patient: Patient
    :param doctor: The doctor object.
    :type doctor: Doctor
    """
    appointmentRequest = AppointmentRequest.objects.get(patient=patient, doctor=doctor)
    appointmentRequest.delete()

def requestAppointment(patient, doctor):
    """
    Sends an appointment request from a patient to a doctor.

    :param patient: The patient object.
    :type patient: Patient
    :param doctor: The doctor object.
    :type doctor: Doctor
    """
    appointmentRequest = AppointmentRequest(patient=patient, doctor=doctor)
    appointmentRequest.save()
    logger.logAppointmentRequest(appointmentRequest)

def requestAppointmentRemoval(patient, doctor):
    """
    Sends a request for appointment removal from a patient to a doctor.

    :param patient: The patient object.
    :type patient: Patient
    :param doctor: The doctor object.
    :type doctor: Doctor
    """
    appointmentRequest = AppointmentRemovalRequest(patient=patient, doctor=doctor)
    appointmentRequest.save()
    logger.logAppointmentRemovalRequest(appointmentRequest)

def removeAppointmentRequest(patient, doctor):
    """
    Removes an appointment request.

    :param patient: The patient object.
    :type patient: Patient
    :param doctor: The doctor object.
    :type doctor: Doctor
    """
    appointmentRequest = AppointmentRequest.objects.get(patient=patient, doctor=doctor)
    appointmentRequest.delete()

def getPatientRemovalRequests(patient):
    """
    Retrieves all appointment removal requests for a patient.

    :param patient: The patient object.
    :type patient: Patient
    :return: All appointment removal requests for the patient.
    :rtype: QuerySet
    """
    return AppointmentRemovalRequest.objects.filter(patient = patient)

def getDoctorRemovalRequests(doctor):
    """
    Retrieves all appointment removal requests for a doctor.

    :param doctor: The doctor object.
    :type doctor: Doctor
    :return: All appointment removal requests for the doctor.
    :rtype: QuerySet
    """
    return AppointmentRemovalRequest.objects.filter(doctor = doctor)

def approveRemoval(patient, doctor):
    """
    Approves an appointment removal request and removes the appointment between a patient and a doctor.

    :param patient: The patient object.
    :type patient: Patient
    :param doctor: The doctor object.
    :type doctor: Doctor
    """
    removalRequest = AppointmentRemovalRequest.objects.get(patient=patient, doctor=doctor)
    removalRequest.delete()
    removeAppointment(patient, doctor)

def declineRemoval(patient, doctor):
    """
    Declines an appointment removal request.

    :param patient: The patient object.
    :type patient: Patient
    :param doctor: The doctor object.
    :type doctor: Doctor
    """
    removalRequest = AppointmentRemovalRequest.objects.get(patient=patient, doctor=doctor)
    removalRequest.delete()

def reEncryptRecord(encrypted_record,username):
    decrypted_record_name = server_encryption.decrypt(encrypted_record.record_name)
    decrypted_record_content = server_encryption.decrypt(encrypted_record.record_content)
    reencrypted_name = server_encryption.encrypt(decrypted_record_name,username)
    reencrypted_content = server_encryption.encrypt(decrypted_record_content,username)
    encrypted_record.record_name = reencrypted_name
    encrypted_record.record_content = reencrypted_content
    return encrypted_record

def getPatientsRecords(patient,username):
    """
    Retrieves all medical records for a patient.

    :param patient: The patient object.
    :type patient: Patient
    :return: All medical records for the patient.
    :rtype: QuerySet
    """
    records = MedicalRecord.objects.filter(patient = patient)
    for encrypted_record in records:
        encrypted_record = reEncryptRecord(encrypted_record, username)
    return records

def getRecordRequests(patient,username):
    """
    Retrieves all record management requests for a patient.

    :param patient: The patient object.
    :type patient: Patient
    :return: All record management requests for the patient.
    :rtype: QuerySet
    """
    requests = RecordManagmentRequest.objects.filter(record_id__patient = patient)
    for request in requests:
        request.record = reEncryptRecord(request.record, username)
    return requests

def uploadRecord(patient, file_name, file_content, request, username):
    """
    Uploads a medical record for a patient.

    :param patient: The patient object.
    :type patient: Patient
    :param file_name: The name of the file.
    :type file_name: str
    :param file_content: The content of the file.
    :type file_content: str
    """
    signature = signatures.sign_text(file_content,securityHandler.getPrivateKey(username))
    newRecord = MedicalRecord(patient=patient, record_name = file_name, record_content = file_content,signature = base64.b64encode(signature).decode('utf-8'), public_key = securityHandler.getRawPublicKey(username))
    newRecord.save()
    currentUser = request.user
    isDoctor = currentUser.groups.filter(name="Doctor").exists()
    if isDoctor:
        logger.logDoctorRecordUpload(currentUser, newRecord)
    else:
        logger.logPatientRecordUpload(newRecord)

def getRecord(record_id, username):
    """
    Retrieves a medical record by its ID.

    :param record_id: The ID of the medical record.
    :type record_id: int
    :return: The medical record object.
    :rtype: MedicalRecord
    """
    encrypted_record =  MedicalRecord.objects.get(record_id=record_id)
    reEncryptRecord(encrypted_record,username)

    return encrypted_record

def editRecordContent(record_id, encrypted_content, request, username):
    """
    Edits the content of a medical record.

    :param record_id: The ID of the medical record.
    :type record_id: int
    :param encrypted_content: The new encrypted content of the record.
    :type content: str
    """
    record = MedicalRecord.objects.get(record_id = record_id)
    if signatures.verify_signature(record.record_content,base64.b64decode(record.signature),securityHandler.getPublicKey(record.public_key)) == True:
        record.record_content = encrypted_content
        record.public_key = securityHandler.getRawPublicKey(username)
        record.signature = base64.b64encode(signatures.sign_text(encrypted_content,securityHandler.getPrivateKey(username))).decode('utf-8')
        record.save()
        currentUser = request.user
        isDoctor = currentUser.groups.filter(name="Doctor").exists()
        if isDoctor:
            logger.logDoctorRecordEdit(currentUser, record)
        else:
            logger.logPatientRecordEdit(record)
    else:
        print("signature aren't the same")

def deleteRecord(record_id, request):
    """
    Deletes a medical record.

    :param record_id: The ID of the medical record.
    :type record_id: int
    """
    currentUser = request.user
    record = getRecord(record_id, currentUser.username)
    isDoctor = currentUser.groups.filter(name="Doctor").exists()
    if isDoctor:
        logger.logDoctorRecordDelete(currentUser, record)
    else:
        logger.logPatientRecordDelete(record)
    record.delete()

def getDoctorsPatientsRecords(doctor,username):
    """
    Retrieves all medical records for the patients appointed to a doctor.

    :param doctor: The doctor object.
    :type doctor: Doctor
    :return: All medical records for the doctor's patients.
    :rtype: QuerySet
    """
    patients = getDoctorsAppointedPatients(doctor).values_list('patient', flat=True)
    records = []
    for patient in patients:
        record = getPatientsRecords(patient,username)
        records += record
    print('records:', records)
    return records

def getDoctorsManagmentRequests(doctor):
    """
    Retrieves all record management requests for a doctor.

    :param doctor: The doctor object.
    :type doctor: Doctor
    :return: All record management requests for the doctor.
    :rtype: QuerySet
    """
    requests =  RecordManagmentRequest.objects.filter(doctor=doctor)
    for request in requests:
        request.record = reEncryptRecord(request.record,doctor)
    return requests

def requestManagmentRights(doctor, record):
    request = RecordManagmentRequest(record=record, doctor=doctor)
    request.save()
    logger.logManagmentRequest(request)

def canManageFile(doctor, record_id,username):
    record = getRecord(record_id,username)
    right =  ApprovedRecordManagmentRequest.objects.filter(record=record, doctor=doctor)
    return len(right) != 0

def hasAlreadyRequestedRights(doctor, record_id,username):
    record = getRecord(record_id,username)
    request = RecordManagmentRequest.objects.filter(record=record, doctor=doctor)
    return len(request) != 0

def grantManagmentRights(record_id,username):
    record = getRecord(record_id,username)
    request = RecordManagmentRequest.objects.get(record=record)
    right = ApprovedRecordManagmentRequest(record = record, doctor = request.doctor)
    right.save()
    logger.logManagmentGranted(request)
    request.delete()

def declineManagmentRequest(record_id,username):
    record = getRecord(record_id,username)
    request = RecordManagmentRequest.objects.get(record=record)
    request.delete()

def getUploadRights(doctor):
    """
    Retrieves the upload rights for a doctor.
    This function retrieves the upload rights for a specific doctor. The upload rights determine whether the doctor has the permission to upload medical records for the patients they are appointed to.
    :param doctor: The doctor object.
    :type doctor: Doctor
    :return: A list of dictionaries containing the upload rights for each patient appointed to the doctor.
    :rtype: list[dict]
    """
    appointments = getDoctorsAppointedPatients(doctor).select_related('patient')
    uploadRights = []
    for appointment in appointments:
        values = {}
        hasRight = ApprovedUploadRequest.objects.filter(patient = appointment.patient, doctor = doctor)
        alreadyAsked = UploadRightRequest.objects.filter(patient = appointment.patient, doctor = doctor)

        values['patient'] = appointment.patient
        values['hasRight'] = hasRight.exists()
        values['alreadyAsked'] = hasRight.exists() or alreadyAsked.exists()

        uploadRights.append(values);
    return uploadRights

def requestUploadRights(doctor, patient):
    request = UploadRightRequest(doctor = doctor, patient = patient)
    request.save()
    logger.logUploadRequest(request)

def getUploadRightRequests(doctor):
    requests = UploadRightRequest.objects.filter(doctor=doctor)
    return requests

def getPatientUploadRightRequests(patient):
    requests = UploadRightRequest.objects.filter(patient=patient)
    return requests

def declineUploadRequest(patient, doctor):
    """
    Declines a record management request.

    :param doctor: The doctor object.
    :type doctor: Doctor
    :param patient: The patient object.
    :type patient: Patient
    :param record: The medical record object.
    :type record: MedicalRecord
    """
    request = UploadRightRequest.objects.get(patient=patient, doctor=doctor)
    request.delete()

def approveUploadRequest(patient, doctor):
    """
    Approves a record management request and grants access to a medical record for a doctor.

    :param doctor: The doctor object.
    :type doctor: Doctor
    :param patient: The patient object.
    :type patient: Patient
    :param record: The medical record object.
    :type record: MedicalRecord
    """
    right = ApprovedUploadRequest(patient=patient, doctor=doctor)
    right.save()
    declineUploadRequest(patient, doctor)
    logger.logUploadRequest(right)

def clearExpiredRights():
    current_time = datetime.now()
    managmentRequests = ApprovedRecordManagmentRequest.objects.filter(until__lt=current_time)
    managmentRequests.delete()
    uploadRequests = ApprovedUploadRequest.objects.filter(until__lt = current_time)
    uploadRequests.delete()

def createOriginalAdmin():
    alreadyExists = User.objects.filter(username = 'admin').exists()
    if not alreadyExists:
        user = User.objects.create_user('admin', password='admin')
        user.is_superuser = False
        user.is_staff = False
        user.email = "admin@gmail.com"
        user.save()
        birth_date = datetime.date(2000, 12, 12)
        addToAdminWaitingList(user, birth_date)
        waitingAdmin = getAdminsWaitingList().get(waitingAdmin = user)
        approveAdmin(user, waitingAdmin)


def check_user_login(username, message):
    user = get_object_or_404(User, username=username)
    
    # Check if the message is "Failed"
    if message == "Failed":
        try:
            user_login = UserLoginFail.objects.get(user=user)
        except UserLoginFail.DoesNotExist:
            # User is not in the table, add a new entry
            UserLoginFail.objects.create(user=user, times=1, connectionTime=timezone.now())
            return "True"
        
        # User is in the table, check the times and connection time
        if user_login.times >= 3:
            # Check if the timeout has passed
            time_difference = timezone.now() - user_login.connectionTime
            if time_difference.total_seconds() > 60:
                # Timeout has passed, reset the times and connectionTime
                user_login.times = 0
                user_login.connectionTime = timezone.now()
                user_login.save()
                return "True"
            else:
                # Still in timeout
                return "False"
        
        # Increment the times and update the connectionTime
        user_login.times += 1
        user_login.connectionTime = timezone.now()
        user_login.save()
        return "True"
    
    else:
        try:
            user_login = UserLoginFail.objects.get(user=user)
        except UserLoginFail.DoesNotExist:
            # User is not in the table, can proceed with login
            return "True"
        
        # User is in the table, check if still in timeout
        if user_login.times >= 3:
            # Check if still in timeout
            time_difference = timezone.now() - user_login.connectionTime
            if time_difference.total_seconds() <= 60:
                # Still in timeout
                return "False"
        
        # Not in timeout, can proceed with login
        return "True"