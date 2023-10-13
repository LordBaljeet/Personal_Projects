"""
URL configuration for Hospital project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from ClientSide.views import *

urlpatterns = [
    path('admin/', admin.site.urls),
    path('logout/', logout_view, name='logout'),
    path('', HomeView, name='home'),
    path('addRecord/', addRecordView, name='addRecord'),
    path('records/', consultRecordsView, name='medical records'),
    path('patients_records/', consultPatientsRecordsView, name='consult patients records'),
    path('consult_record/', consultRecordView, name='consult record'),
    path('consult_patient_record/', consultPatientRecordView, name='consult patient record'),
    path('doctors/', ConsultDoctorsView, name='Consult Doctors'),
    path('patients/', ConsultPatientsView, name='Consult Patients'),
    path('login/', LoginView, name='login'),
    path('choice/', GroupChoiceView,name='group choice'),
    path('patient/signup/', PatientRegisterView, name='patient signup'),
    path('doctor/signup/', DoctorRegisterView, name='doctor signup'),
    path('administrator/signup/', AdminRegisterView, name='admin signup'),
    path('approvalRequests/', ApprovalRequestsView, name='approval requests'),
    path('verification/', VerificationPage, name='verification'),
    path('wait', WaitingView, name='wait'),
]
