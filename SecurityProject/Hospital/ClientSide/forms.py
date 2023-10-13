from django import forms  
from django.contrib.auth.forms import UserCreationForm 
from django.contrib.auth import get_user_model
from django.forms.widgets import TextInput, FileInput
from django.core.validators import RegexValidator, FileExtensionValidator
from datetime import date

class DateInput(TextInput):
    """
    A custom form widget for inputting dates.

    This widget sets the input type as 'date' for HTML5 date input support.

    """
    input_type = 'date'

class PatientRegistrationForm(UserCreationForm):
    """
    A form for patient registration.

    - Inherits from UserCreationForm, which provides the necessary fields for user registration.
    - Includes an additional field 'birth_date' for the patient's birth date.
    - Provides validation for the birth date field to ensure the patient is at least 18 years old.

    """
    class Meta:
        model = get_user_model()
        fields = ('username','first_name', 'last_name', 'email', 'password1', 'password2')
    birth_date = forms.DateField(label='Birth Date', widget=DateInput)

    def clean_birth_date(self):
        """
        Validates the birth date field to ensure the patient is at least 18 years old.

        Returns:
            date: The validated birth date.

        Raises:
            ValidationError: If the patient is not at least 18 years old.

        """
        birth_date = self.cleaned_data.get('birth_date')
        if birth_date.year + 18 > date.today().year:
            raise forms.ValidationError("You must be at least 18 years old.")

        return birth_date

class DoctorRegistrationForm(UserCreationForm):
    """
    A form for doctor registration.

    - Inherits from UserCreationForm, which provides the necessary fields for user registration.
    - Includes an additional field 'inami' for the Belgian INAMI code.
    - Provides validation for the INAMI code field to ensure it matches the required format.

    """
    class Meta:
        model = get_user_model()
        fields = ('username','first_name', 'last_name', 'email', 'password1', 'password2')
    inami = forms.CharField(label='INAMI', validators=[
        RegexValidator(r'^\d{11}$', 'The Belgian INAMI code must be 11 digits long.'),
        RegexValidator(r'^[0-9]+$', 'The Belgian INAMI code must contain only digits.'),
    ])

class AdminRegistrationForm(UserCreationForm):
    """
    A form for admin registration.

    - Inherits from UserCreationForm, which provides the necessary fields for user registration.
    - Includes an additional field 'birth_date' for the admin's birth date.
    - Provides validation for the birth date field to ensure the admin is at least 18 years old.

    """
    class Meta:
        model = get_user_model()
        fields = ('username','first_name', 'last_name', 'email', 'password1', 'password2')
    birth_date = forms.DateField(label='Birth Date', widget=DateInput)
    def clean_birth_date(self):
        """
        Validates the birth date field to ensure the admin is at least 18 years old.

        Returns:
            date: The validated birth date.

        Raises:
            ValidationError: If the admin is not at least 18 years old.

        """
        birth_date = self.cleaned_data.get('birth_date')
        if birth_date.year + 18 > date.today().year:
            raise forms.ValidationError("You must be at least 18 years old.")

        return birth_date
    
def file_size_validator(value):
    max_size = 5 * 1024 * 1024
    if value.size > max_size:
        raise forms.ValidationError('File too large. Size should not exceed 5 MiB.')

class RecordUploadForm(forms.Form):
    """
    A form for record upload.

    - Includes a single field 'file' for uploading a file.
    - Provides validation to ensure the uploaded file has the '.txt' extension.

    """
    file = forms.FileField(label='File', widget=FileInput, validators = [
        FileExtensionValidator( ['txt'], "Must be a txt file" ), file_size_validator
        ])