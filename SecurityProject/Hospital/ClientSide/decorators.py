from django.http import HttpResponse
from django.shortcuts import redirect, render
import ServerSide.accessport as accessport

def unauthenticated_user(view_func):
    """
    Decorator for views that redirects authenticated users to the home page.

    Args:
        view_func (function): The view function to be decorated.

    Returns:
        function: The decorated view function.

    """
    def wrapper_func(request, *args, **kwargs):
        if request.user.is_authenticated:
            return redirect("home")
        else:
            return view_func(request, *args, **kwargs)
    return wrapper_func

def alreadyApproved(view_func):
    """
    Decorator for views that redirects users already approved in admin or doctor waiting lists to a waiting page.

    Args:
        view_func (function): The view function to be decorated.

    Returns:
        function: The decorated view function.

    """
    def wrapper_func(request, *args, **kwargs):
        isInAdminList = accessport.isInAdminWaitingList(request.user)
        isInDoctorList = accessport.isInDoctorWaitingList(request.user)
        if isInAdminList or isInDoctorList:
            return redirect('wait')
        else:
            return view_func(request, *args, **kwargs)
    return wrapper_func

def patientOnly(view_func):
    """
    Decorator for views that redirects non-patient users to the home page.

    Args:
        view_func (function): The view function to be decorated.

    Returns:
        function: The decorated view function.

    """
    def wrapper_func(request, *args, **kwargs):
        if not request.user.groups.filter(name="Patient").exists():
            return redirect('home')
        else:
            return view_func(request, *args, **kwargs)
    return wrapper_func

def adminOnly(view_func):
    """
    Decorator for views that redirects non-admin users to the home page.

    Args:
        view_func (function): The view function to be decorated.

    Returns:
        function: The decorated view function.

    """
    def wrapper_func(request, *args, **kwargs):
        if not request.user.groups.filter(name="Admin").exists():
            return redirect('home')
        else:
            return view_func(request, *args, **kwargs)
    return wrapper_func

def doctorOnly(view_func):
    """
    Decorator for views that redirects non-doctor users to the home page.

    Args:
        view_func (function): The view function to be decorated.

    Returns:
        function: The decorated view function.

    """
    def wrapper_func(request, *args, **kwargs):
        if not request.user.groups.filter(name="Doctor").exists():
            return redirect('home')
        else:
            return view_func(request, *args, **kwargs)
    return wrapper_func