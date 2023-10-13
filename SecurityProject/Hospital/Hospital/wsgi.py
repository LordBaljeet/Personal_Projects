"""
WSGI config for Hospital project.

It exposes the WSGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/4.2/howto/deployment/wsgi/
"""

import os
import ClientSide.securityHandler as clientSide_Security
import ServerSide.securityHandler as serverSide_Security
import ServerSide.accessport as accessport
from django.core.wsgi import get_wsgi_application

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'Hospital.settings')

clientSide_Security.generateKeys()

serverSide_Security.generateKeys()
serverSide_Security.generate_aes_key()

accessport.createOriginalAdmin()

# accessport.clearExpiredRights()

application = get_wsgi_application()