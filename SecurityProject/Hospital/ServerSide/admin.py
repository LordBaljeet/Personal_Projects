from django.contrib import admin
from .models import *

admin.site.register(Patient)
admin.site.register(Doctor)
admin.site.register(Admin)
admin.site.register(AdminWaitingList)
admin.site.register(DoctorWaitingList)
admin.site.register(MedicalRecord)
admin.site.register(RecordManagmentRequest)
admin.site.register(ApprovedRecordManagmentRequest)
admin.site.register(Appointed)
admin.site.register(AppointmentRequest)
admin.site.register(AppointmentRemovalRequest)
admin.site.register(UploadRightRequest)
admin.site.register(ApprovedUploadRequest)
# Register your models here.
