import random
import time
import ClientSide.securityHandler
from django.shortcuts import render,redirect
from django.contrib.auth import authenticate,login, logout
from django.contrib.auth.decorators import login_required
from django.views.decorators.http import require_POST
from django.views.decorators.csrf import csrf_protect
from .decorators import *
from .forms import *
import ClientSide.securityHandler as client_encryption
from django.core.files.base import ContentFile as DjangoFile
from django.http import FileResponse
from . import securityHandler
from django.conf import settings
from django.contrib.auth.models import User

@login_required(login_url='login')
@alreadyApproved
def HomeView(request):
    """
    Renders the home page based on the user's role.

    - If the user is a Patient, it renders the "Patienthome.html" template.
    - If the user is a Doctor, it renders the "Doctorhome.html" template.
    - If the user is an Admin, it renders the "Adminhome.html" template.
    - If the user doesn't have any specific role, it renders the "home.html" template.

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template.
    """
    if(request.method == 'POST'):
        action = request.POST.get('action')
        if(action == 'logout'):
            logout(request)
        return redirect('login')

    username = request.user.username
    context = {'username': username}
    if context is not None:
        if request.user.groups.filter(name='Patient').exists():
            return render(request,'Patienthome.html',context)
        if request.user.groups.filter(name='Doctor').exists():
            return render(request,'Doctorhome.html',context)
        if request.user.groups.filter(name='Admin').exists():
            return render(request,'Adminhome.html',context)
        
    return render(request,'home.html', context)

def VerificationPage(request):
    if request.method == 'POST':
        entered_code = request.POST.get('code')
        stored_code = request.session.get('login_code')
        if stored_code is None:
            redirect('login')
        stored_timestamp = request.session.get('login_code_timestamp')
        username = request.session.get('login_username')
        if stored_code and stored_timestamp:
            current_timestamp = time.time()
            time_diff = current_timestamp - stored_timestamp

            # Check if the code has expired (1 minute timeout)
            if time_diff > 60:
                return render(request, 'login.html', {'error_message': 'Verification code has expired'})

            if entered_code == stored_code:
                user = User.objects.get(username=username)
                login(request, user)
                # Clear the session variables
                del request.session['login_code']
                del request.session['login_username']
                return redirect('home')
            else:
                return render(request, 'verification.html', {'error_message': 'Invalid verification code'})
    else:
        return render(request, 'verification.html')

@csrf_protect
@unauthenticated_user
def LoginView(request):
    """
    Handles the login functionality.

    - If the request method is POST, it validates the login credentials and logs in the user.
    - If the request method is GET, it renders the login page.

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template or redirecting to the home page.
    """
    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')
        try:
            user = accessport.getUser(username)
        except User.DoesNotExist:
            return render(request, 'login.html', {'error_message': 'Incorrect username'})
        
        user = authenticate(request, username=username, password=password)
        if user is not None:
            if accessport.check_user_login(username,"Passed") == "True":
                code = str(random.randint(100000, 999999))
                # Save the code in user's session
                request.session['login_code'] = code
                request.session['login_code_timestamp'] = time.time()  # Store the current timestamp
                request.session['login_username'] = username
                
                # Send the code to user's email, not working
                subject = 'Login Verification Code'
                message = f'Your verification code is: {code}'
                from_email = 'no-reply@example.com'
                user.email_user(subject, message, from_email)
                
                # Redirect to the verification page
                return redirect('verification')
            else:
                return render(request, 'login.html', {'error_message': 'You are in a timeout'})
        else:
            if(accessport.check_user_login(username,"Failed")) != "True":
                    return render(request, 'login.html', {'error_message': 'You are in a timeout'})
                
            return render(request, 'login.html', {'error_message': 'Invalid login credentials'})
    else:
        return render(request, 'login.html')

@unauthenticated_user
def GroupChoiceView(request):
    """
    Renders the group choice page where the user can select their role (Patient, Doctor, Admin).

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template.
    """
    return render(request,"groupChoice.html")

@csrf_protect
@unauthenticated_user
def PatientRegisterView(request):
    """
    Handles the patient registration functionality.

    - If the request method is POST, it validates the registration form data and registers the patient.
    - If the request method is GET, it renders the signup page with the PatientRegistrationForm.

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template or redirecting to the login page.
    """
    form = PatientRegistrationForm()
    if(request.method == 'POST'):
        form = PatientRegistrationForm(request.POST)
        if(form.is_valid()):
            birth_date = form.cleaned_data['birth_date']
            # Register User
            user = form.save()
            # Register Patient
            accessport.registerPatient(user, birth_date)
            print(securityHandler.generateKeysUser(user.username))
            return redirect('login')

    return render(request, 'signup.html', {
        'form': form
    });

@csrf_protect
@unauthenticated_user
def DoctorRegisterView(request):
    """
    Handles the doctor registration functionality.

    - If the request method is POST, it validates the registration form data and registers the doctor.
    - If the request method is GET, it renders the signup page with the DoctorRegistrationForm.

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template or redirecting to the wait page.
    """
    form = DoctorRegistrationForm()
    if(request.method == 'POST'):
        form = DoctorRegistrationForm(request.POST)
        if(form.is_valid()):
            inami = form.cleaned_data["inami"]
            # Register User
            user = form.save()
            # Adding to wait list
            accessport.addToDoctorWaitingList(user, inami)
            print(securityHandler.generateKeysUser(user.username))
            return redirect('wait');

    return render(request, 'signup.html', {
        'form': form
    });

@csrf_protect
@unauthenticated_user
def AdminRegisterView(request):
    """
    Handles the admin registration functionality.

    - If the request method is POST, it validates the registration form data and registers the admin.
    - If the request method is GET, it renders the signup page with the AdminRegistrationForm.

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template or redirecting to the wait page.
    """
    form = AdminRegistrationForm()
    if(request.method == 'POST'):
        form = AdminRegistrationForm(request.POST)
        if(form.is_valid()):
            birth_date = form.cleaned_data['birth_date']
            # Register User
            user = form.save()
            # Adding to wait list
            accessport.addToAdminWaitingList(user, birth_date)
            return redirect('wait');

    return render(request, 'signup.html', {
        'form': form
    });

@login_required(login_url='login')
def WaitingView(request):
    """
    Renders the wait approval page.

    - If the request method is POST, it handles the logout action and redirects to the login page.

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template.
    """
    if(request.method == 'POST'):
        action = request.POST.get('action')
        if(action == 'logout'):
            logout(request)
        return redirect('login')
    return render(request, 'waitApproval.html')

@csrf_protect
@login_required(login_url='login')
@alreadyApproved
@adminOnly
def ApprovalRequestsView(request):
    """
    Renders the approval requests page.

    - Retrieves the waiting doctors, waiting admins, and all users.
    - If the request method is POST, it handles the approval or decline decision for doctors and admins.

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template.
    """
    doctors = accessport.getDoctorsWaitingList()
    admins = accessport.getAdminsWaitingList()
    users = accessport.getUsers()

    if request.method == 'POST':
        username = request.POST.get('username')
        user = accessport.getUser(username)
        decision = request.POST.get('decision')
        isAdmin = accessport.isInAdminWaitingList(user)
        isDoctor = accessport.isInDoctorWaitingList(user)

        if isDoctor and decision == 'approved':
            doctor = doctors.get(waitingDoctor = user)
            accessport.approveDoctor(doctor, request)
        elif isDoctor:
            doctor = doctors.get(waitingDoctor = user)
            accessport.declineDoctor(doctor)
        elif isAdmin and decision == 'approved':
            admin = admins.get(waitingAdmin = user)
            accessport.approveAdmin(user,admin)
        elif isAdmin:
            admin = admins.get(waitingAdmin = user)
            accessport.declineAdmin(admin)
        else:
            accessport.deleteUser(user, request)

    context = {
        'doctors': doctors,
        'admins': admins,
        'users': users
    }

    return render(request, 'approvalRequests.html', context)

@login_required(login_url='login')
@csrf_protect
@patientOnly
def ConsultDoctorsView(request):
    """
    Handles the consultation of doctors functionality for patients.

    - Retrieves the patient, not appointed doctors, appointed doctors, appointment requests, and removal requests.
    - If the request method is POST, it handles the appointment and removal requests, as well as appointment and removal decisions.

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template.
    """
    patient = accessport.getPatient(request.user)
    notAppointedDoctors = accessport.getPatientsNotAppointedDoctors(patient)
    appointedDoctors = accessport.getPatientsAppointedDoctors(patient)
    requests = accessport.getPatientAppointmentRequests(patient=patient)
    removalRequests = accessport.getPatientRemovalRequests(patient=patient)
    if request.method == 'POST':
        username = request.POST.get('username')
        appointDoctor = request.POST.get('appointDoctor')
        removeDoctor = request.POST.get('removeDoctor')
        appointmentDecision = request.POST.get('appointmentDecision')
        removalDecision = request.POST.get('removalDecision')

        if appointDoctor:
            user = accessport.getUser(appointDoctor)
            doctor = accessport.getDoctor(user)
            accessport.appointDoctor(patient, doctor)
            notAppointedDoctors = accessport.getPatientsNotAppointedDoctors(patient)
        elif removeDoctor:
            user = accessport.getUser(removeDoctor)
            doctor = accessport.getDoctor(user)
            accessport.removeAppointment(patient, doctor)
        elif appointmentDecision == 'approved':
            user = accessport.getUser(username)
            doctor = accessport.getDoctor(user)
            accessport.approveAppointment(patient, doctor)
        elif appointmentDecision == 'declined':
            user = accessport.getUser(username)
            doctor = accessport.getDoctor(user)
            accessport.declineAppointment(patient, doctor)
        elif removalDecision == 'approved':
            user = accessport.getUser(username)
            doctor = accessport.getDoctor(user)
            accessport.approveRemoval(patient, doctor)
        else:
            user = accessport.getUser(username)
            doctor = accessport.getDoctor(user)
            accessport.declineRemoval(patient, doctor)

    context = {
        'appointed': appointedDoctors,
        'notAppointed': notAppointedDoctors,
        'requests': requests,
        'removalRequests': removalRequests,
        'username': request.user.username
    }

    return render(request, 'consultDoctors.html', context)

@login_required(login_url='login')
@csrf_protect
@doctorOnly
def ConsultPatientsView(request):
    """
    Handles the consultation of patients functionality for doctors.

    - Retrieves the doctor, not appointed patients, appointed patients, appointment requests, and appointment removal requests.
    - If the request method is POST, it handles appointment and removal requests, as well as cancellation of requests.

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template.
    """
    doctor = accessport.getDoctor(request.user)
    notAppointedPatients = accessport.getDoctorsNotAppointedPatients(doctor)
    appointedPatients = accessport.getDoctorsAppointedPatients(doctor)
    appointmentRequests = accessport.getDoctorAppointmentRequests(doctor=doctor)
    appointmentRemovalRequests = accessport.getDoctorRemovalRequests(doctor=doctor)

    if request.method == 'POST':
        requestAppointment = request.POST.get('requestAppointment')
        requestRemoval = request.POST.get('requestRemoval')
        cancelRequest = request.POST.get('cancelRequest')
        cancelRemoval = request.POST.get('cancelRemoval')

        if requestAppointment:
            user = accessport.getUser(requestAppointment)
            patient = accessport.getPatient(user)
            accessport.requestAppointment(patient, doctor)
        elif requestRemoval:
            user = accessport.getUser(requestRemoval)
            patient = accessport.getPatient(user)
            accessport.requestAppointmentRemoval(patient, doctor)
        elif cancelRequest:
            user = accessport.getUser(cancelRequest)
            patient = accessport.getPatient(user)
            accessport.removeAppointmentRequest(patient, doctor)
        else:
            user = accessport.getUser(cancelRemoval)
            patient = accessport.getPatient(user)
            accessport.declineRemoval(patient, doctor)

    context = {
        'appointed': appointedPatients,
        'notAppointed': notAppointedPatients,
        'appointmentRequests': appointmentRequests,
        'appointmentRemovalRequests': appointmentRemovalRequests,
        'username': request.user.username
    }

    return render(request, 'consultPatients.html', context)

@login_required(login_url='login')
@csrf_protect
@patientOnly
def consultRecordsView(request):
    """
    Handles the consultation of records functionality for patients.

    - Retrieves the patient, encrypted records, decrypted records, management requests, and upload requests.
    - If the request method is POST, it handles management request approvals and declines, as well as upload request approvals and declines.

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template.
    """
    patient = accessport.getPatient(request.user)
    if request.method == 'POST':
        approveManagment = request.POST.get('approveManagment')
        declineManagment = request.POST.get('declineManagment')
        approveUpload = request.POST.get('approveUpload')
        declineUpload = request.POST.get('declineUpload')

        if approveManagment:
            accessport.grantManagmentRights(approveManagment,request.user.username)
        elif declineManagment:
            accessport.declineManagmentRequest(declineManagment,request.user.username)
        elif approveUpload:
            user = accessport.getUser(approveUpload)
            doctor = accessport.getDoctor(user)
            accessport.approveUploadRequest(patient, doctor)
        elif declineUpload:
            user = accessport.getUser(approveUpload)
            doctor = accessport.getDoctor(user)
            accessport.declineUploadRequest(patient, doctor)

    encrypted_records = accessport.getPatientsRecords(patient,request.user.username)
    records = client_encryption.decrypt_records(encrypted_records,request.user.username)
    managmentRequests = accessport.getRecordRequests(patient,request.user.username)
    managmentRequests = client_encryption.decrypt_managment_requests(managmentRequests,request.user.username)
    uploadRequests = accessport.getPatientUploadRightRequests(patient)
    context = {
        'records': records,
        'managmentRequests': managmentRequests,
        'uploadRequests': uploadRequests
    }

    return render(request, 'consultRecords.html', context)

@login_required(login_url='login')
@patientOnly
@csrf_protect
def addRecordView(request):
    """
    Handles the addition of a record functionality for patients.

    - If the request method is POST, it validates the record upload form data and uploads the record.
    - If the request method is GET, it renders the add record page with the RecordUploadForm.

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template.
    """
    if request.method == 'POST':
        form = RecordUploadForm(request.POST, request.FILES)
        if form.is_valid():
            file = form.cleaned_data['file']
            encrypted_name, encrypted_content = client_encryption.encrypt_file(file)
            patient = accessport.getPatient(request.user)
            accessport.uploadRecord(patient, encrypted_name, encrypted_content, request,request.user.username)
        else:
            print('not valid')
    form = RecordUploadForm()
    context = {
        'form': form,
    }
    return render(request, 'addRecord.html', context)

@login_required(login_url='login')
@csrf_protect
@patientOnly
def consultRecordView(request):
    """
    Handles the consultation of a single record functionality for patients.

    - If the request method is POST:
        - If a record ID is provided, it retrieves the encrypted record, decrypts it, and renders the consult record page.
        - If the 'save' button is clicked, it encrypts the record content and updates the record.
        - If the 'delete' button is clicked, it deletes the record.
    - If the request method is GET, it redirects to the home page.

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template or redirecting to the home page.
    """
    if request.method == 'POST':
        record_id = request.POST.get('record_id')
        if record_id :
            encrypted_record = accessport.getRecord(record_id,request.user.username)
            record = client_encryption.decrypt_record(encrypted_record,request.user.username)
            context = {
                'record': record,
                'canManageFile': True
            }
            return render(request, 'consultRecord.html', context)
        elif request.POST.get('save'):
            content = request.POST.get('record_content')
            encrypted_content = client_encryption.encrypt(content)
            id = request.POST.get('save')
            accessport.editRecordContent(id, encrypted_content, request,request.user.username)
        elif request.POST.get('delete'):
            content = request.POST.get('record_content')
            id = request.POST.get('delete')
            accessport.deleteRecord(id, request)
        elif request.POST.get('download'):
            record_id = request.POST.get('download')
            encrypted_record = accessport.getRecord(record_id,request.user.username)
            record = client_encryption.decrypt_record(encrypted_record,request.user.username)
            file = DjangoFile(content = record.record_content.encode('utf-8'), name = record.record_name)
            response = FileResponse(file)
            response['Content-Disposition'] = f'attachment; filename="{record.record_name}"'
            return response
        
    return redirect('home')

@login_required(login_url='login')
@csrf_protect
@doctorOnly
def consultPatientsRecordsView(request):
    """
    Handles the consultation of patient records functionality for doctors.

    - Retrieves the doctor, management requests, upload requests, encrypted records, decrypted records, and upload rights.
    - If the request method is POST, it handles the deletion of management and upload requests, record uploads, and upload right requests.

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template.
    """
    doctor = accessport.getDoctor(request.user)
    if request.method == 'POST':
        delete_managment_request = request.POST.get('delete_managment_request')
        delete_upload_request = request.POST.get('delete_upload_request')
        upload = request.POST.get('upload')
        uploadRequest = request.POST.get('request')

        if delete_managment_request:
            accessport.declineManagmentRequest(delete_managment_request)
        elif delete_upload_request:
            user = accessport.getUser(delete_upload_request)
            patient = accessport.getPatient(user)
            accessport.declineUploadRequest(patient, doctor)
        elif upload:
            file = request.FILES.get('file')
            encrypted_name, encrypted_content = client_encryption.encrypt_file(file)
            user = accessport.getUser(upload)
            patient = accessport.getPatient(user)
            accessport.uploadRecord(patient, encrypted_name, encrypted_content, request, request.user.username)
        elif uploadRequest:
            user = accessport.getUser(uploadRequest)
            patient = accessport.getPatient(user)
            accessport.requestUploadRights(doctor, patient)

    managmentRequests = accessport.getDoctorsManagmentRequests(doctor)
    managmentRequests = client_encryption.decrypt_managment_requests(managmentRequests,request.user.username)
    uploadRequests = accessport.getUploadRightRequests(doctor)
    encrypted_records = accessport.getDoctorsPatientsRecords(doctor,request.user.username)
    records = client_encryption.decrypt_records(encrypted_records,request.user.username)
    rights = accessport.getUploadRights(doctor)
    context = {
        'records': records,
        'rights': rights,
        'managmentRequests': managmentRequests,
        'uploadRequests': uploadRequests
    }

    return render(request, 'consultPatientsRecords.html', context)

@login_required(login_url='login')
@csrf_protect
@doctorOnly
def consultPatientRecordView(request):
    """
    Handles the consultation of a single patient record functionality for doctors.

    - If the request method is POST:
        - If a record ID is provided, it retrieves the encrypted record, decrypts it, and renders the consult record page.
        - If the 'save' button is clicked, it encrypts the record content and updates the record.
        - If the 'delete' button is clicked, it deletes the record.
        - If the 'requestRights' button is clicked, it requests management rights for the record.
    - If the request method is GET, it redirects to the home page.

    Args:
        request (HttpRequest): The HTTP request object.

    Returns:
        HttpResponse: The HTTP response object containing the rendered template or redirecting to the home page.
    """
    if request.method == 'POST':
        record_id = request.POST.get('record_id')
        if record_id :
            encrypted_record = accessport.getRecord(record_id,request.user.username)
            record = client_encryption.decrypt_record(encrypted_record,request.user.username)
            doctor = accessport.getDoctor(request.user)
            canManageFile = accessport.canManageFile(doctor, record_id,request.user.username)
            hasAlreadyRequestedRights = accessport.hasAlreadyRequestedRights(doctor, record_id,request.user.username)
            context = {
                'record': record,
                'canManageFile': canManageFile,
                'hasAlreadyRequestedRights': hasAlreadyRequestedRights
            }
            return render(request, 'consultRecord.html', context)
        elif request.POST.get('save'):
            content = request.POST.get('record_content')
            encrypted_content = client_encryption.encrypt(content)
            id = request.POST.get('save')
            accessport.editRecordContent(id, encrypted_content, request, request.user.username)
        elif request.POST.get('delete'):
            id = request.POST.get('delete')
            accessport.deleteRecord(id, request)
        elif request.POST.get('requestRights'):
            id = request.POST.get('requestRights')
            doctor = accessport.getDoctor(request.user)
            record = accessport.getRecord(id,request.user.username)
            accessport.requestManagmentRights(doctor, record)
        elif request.POST.get('download'):
            record_id = request.POST.get('download')
            encrypted_record = accessport.getRecord(record_id,request.user.username)
            record = client_encryption.decrypt_record(encrypted_record,request.user.username)
            file = DjangoFile(content = record.record_content.encode('utf-8'), name = record.record_name)
            response = FileResponse(file)
            response['Content-Disposition'] = f'attachment; filename="{record.record_name}"'
            return response

    return redirect('home')

@csrf_protect
@require_POST
def logout_view(request):
    logout(request)
    return HttpResponse(status=200)