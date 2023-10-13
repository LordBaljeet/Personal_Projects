from django.db import models
from django.conf import settings
from datetime import date, timedelta
from django.core.exceptions import ValidationError
from django.utils.translation import gettext_lazy as _
from django.core.validators import *
from django.utils import timezone
from datetime import timedelta



def age_validator(value):
    if(value.year + 18 > date.today().year):
        raise ValidationError("You must be at least 18 years old")

class Patient(models.Model):
    user = models.OneToOneField(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, unique=True)
    birth_date = models.DateField("birth_date", validators = [age_validator])

    def __str__(self):
        return self.user.username
    
class Doctor(models.Model):
    user = models.OneToOneField(settings.AUTH_USER_MODEL,on_delete=models.CASCADE)
    inami = models.CharField("Inami", unique = True, max_length=11,validators=[
        RegexValidator(r'^\d{11}$', 'The Belgian INAMI code must be 11 digits long.'),
        RegexValidator(r'^[0-9]+$', 'The Belgian INAMI code must contain only digits.'),
    ])
    def __str__(self):
        return self.user.username

class Admin(models.Model):
    user = models.OneToOneField(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, unique=True)
    birth_date = models.DateField("birth_date", validators = [age_validator])

    def __str__(self):
        return self.user.username
    
class AdminWaitingList(models.Model):
    waitingAdmin = models.OneToOneField(settings.AUTH_USER_MODEL,on_delete=models.CASCADE)
    birth_date = models.DateField("birth_date", validators = [age_validator])

class DoctorWaitingList(models.Model):
    waitingDoctor = models.OneToOneField(settings.AUTH_USER_MODEL,on_delete=models.CASCADE)
    inami = models.CharField("Inami", unique = True, max_length=11,validators=[
        RegexValidator(r'^\d{11}$', 'The Belgian INAMI code must be 11 digits long.'),
        RegexValidator(r'^[0-9]+$', 'The Belgian INAMI code must contain only digits.'),
    ])

    def __str__(self):
        return self.waitingDoctor

class Appointed(models.Model):
    patient = models.ForeignKey(Patient, on_delete=models.CASCADE)
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE)

    class Meta:
        unique_together = ('patient', 'doctor')
    
    def __str__(self):
        return f'{self.patient} - {self.doctor}'

class AppointmentRequest(models.Model):
    patient = models.ForeignKey(Patient, on_delete=models.CASCADE)
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE)
    
    class Meta:
        unique_together = ('patient', 'doctor')

    def __str__(self):
        return f'{self.patient} - {self.doctor}'

class AppointmentRemovalRequest(models.Model):
    patient = models.ForeignKey(Patient, on_delete=models.CASCADE)
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE)
    
    class Meta:
        unique_together = ('patient', 'doctor')

    def __str__(self):
        return f'{self.patient} - {self.doctor}'

def upload_path(instance, filename):
    return f'user_{instance.patient.user.username}/{filename}'

class MedicalRecord(models.Model):
    record_id = models.AutoField('record_id', primary_key=True)
    patient = models.ForeignKey(Patient, on_delete=models.CASCADE,related_name='owner')
    record_name = models.CharField('record_name', max_length=100)
    record_content = models.TextField('record_content')
    signature = models.TextField('signature', default=None)
    public_key = models.TextField('public_key',default=None)

class RecordManagmentRequest(models.Model):
    record = models.ForeignKey(MedicalRecord, on_delete=models.CASCADE)
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE)

    class Meta:
        unique_together = ('record', 'doctor')

class ApprovedRecordManagmentRequest(models.Model):
    record = models.ForeignKey(MedicalRecord, on_delete=models.CASCADE)
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE)
    until = models.DateTimeField('until', default=timezone.now() + timedelta(hours=1))

    class Meta:
        unique_together = ('record', 'doctor')

class UploadRightRequest(models.Model):
    patient = models.ForeignKey(Patient, on_delete=models.CASCADE)
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE)

    class Meta:
        unique_together = ('patient', 'doctor')

class ApprovedUploadRequest(models.Model):
    patient = models.ForeignKey(Patient, on_delete=models.CASCADE)
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE)
    until = models.DateTimeField('until', default=timezone.now() + timedelta(hours=1))

    class Meta:
        unique_together = ('patient', 'doctor')

class UserLoginFail(models.Model):
    user = models.ForeignKey(settings.AUTH_USER_MODEL,on_delete=models.CASCADE)
    connectionTime = models.DateTimeField("connectionTime",null=False,default=timezone.now)
    times = models.PositiveSmallIntegerField("times")

# Create your models here.
