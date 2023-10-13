from django.conf import settings
import datetime
import os

def createFile():
    if not os.path.exists(settings.LOGS):
        os.mkdir(settings.LOGS)

    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    if not os.path.exists(file_path):
        with open(file_path, 'w') as file:
            # Perform any initial writing or setup if needed
            file.write(f"Logs of {file_name}\n")

def logPatientRecordUpload(record):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Patient {} Uploaded A record with ID {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), record.patient, record.record_id))
            
def logDoctorRecordUpload(doctor, record):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Doctor {} Uploaded A record with ID {} on behalf of {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), doctor, record.record_id, record.patient))

def logPatientRecordEdit(record):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Patient {} edited the record with ID {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), record.patient, record.record_id))
            
def logDoctorRecordEdit(doctor, record):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Doctor {} edited the record with ID {} on behalf of {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), doctor, record.record_id, record.patient))
            
def logPatientRecordDelete(record):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Patient {} deleted the record with ID {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), record.patient, record.record_id))
            
def logDoctorRecordDelete(doctor, record):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Doctor {} deleted the record with ID {} on behalf of {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), doctor, record.record_id, record.patient))
            
def logAppointmentCreated(appointment):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Doctor {} is now appointed to Patient {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), appointment.doctor, appointment.patient))

def logAppointmentDeleted(appointment):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Doctor {} is no longer appointed to Patient {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), appointment.doctor, appointment.patient))
            
def logAppointmentRequest(appointmentRequest):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Doctor {} requested an appointment with Patient {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), appointmentRequest.doctor, appointmentRequest.patient))
            
def logAppointmentRemovalRequest(appointmentRemovalRequest):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Doctor {} requested an appointment removal with Patient {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), appointmentRemovalRequest.doctor, appointmentRemovalRequest.patient))

def logNewPatient(patient):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Patient {} has been added to the list of patients\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), patient))

def logNewAdmin(admin):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Admin {} has been added to the list of admins\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), admin))

def logNewDoctor(doctor):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Doctor {} has been added to the list of doctors\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), doctor))
            
def logUserWaitingApproval(user):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | User {} has been added to the list of waiting users'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), user))
            
def logUserApproval(user, admin):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | User {} has been approved by Admin {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), user, admin))

def logUserDeletion(user, admin):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | User {} has been removed by Admin {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), user, admin))

def logManagmentRequest(managmentRequest):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Doctor {} has requested managment rights for the record with ID {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), managmentRequest.doctor, managmentRequest.record.record_id))
            
def logUploadRequest(uploadRequest):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Doctor {} has requested upload rights from Patient {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), uploadRequest.doctor, uploadRequest.patient))
            
def logManagmentGranted(managmentRequest):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Doctor {} has been granted managment rights on the record with ID {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), managmentRequest.doctor, managmentRequest.record.record_id))

def logUploadGranted(uploadRequest):
    date = datetime.date.today()
    file_name = date.strftime('%d-%m-%y')
    file_path = os.path.join(settings.LOGS, (file_name + '.log'))
    createFile()
    with open(file_path, 'a+') as file:
            # Perform any initial writing or setup if needed
            file.write('- {} | Doctor {} has been granted upload rights by Patient {}\n'
                       .format(datetime.datetime.now().time().strftime('%H : %M : %S'), uploadRequest.doctor, uploadRequest.patient))