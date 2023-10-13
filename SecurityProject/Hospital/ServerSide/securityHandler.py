from cryptography.hazmat.primitives.asymmetric import rsa,padding
from cryptography.hazmat.primitives import serialization,hashes
from django.conf import settings
from cryptography.hazmat.backends import default_backend
import base64,os

#private_key = None
#load_dotenv()
import os
from cryptography.fernet import Fernet

def generate_aes_key():
    aes_key = Fernet.generate_key()
    server_aes_key_path = settings.AESKEY
    if os.path.getsize(server_aes_key_path) == 0:
        with open(server_aes_key_path, "wb") as file:
            file.write(aes_key)

def get_aes_key():
    server_aes_key_path = settings.AESKEY
    with open(server_aes_key_path, "rb") as file:
        aes_key_data = file.read()
    return aes_key_data

def encrypt_data(data, aes_key):
    f = Fernet(aes_key)
    encrypted_data = f.encrypt(data.encode())
    return encrypted_data

def decrypt_data(encrypted_data, aes_key):
    f = Fernet(aes_key)
    decrypted_data = f.decrypt(encrypted_data).decode()
    return decrypted_data


def generateKeys():
    global private_key
    # Generate a new RSA key pair
    private_key = rsa.generate_private_key(
        public_exponent=65537,
        key_size=2048
    )
    private_key_pem = private_key.private_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PrivateFormat.PKCS8,
        encryption_algorithm=serialization.NoEncryption()
    )

    # Write the private key to the .env file if it is empty
    server_private_key_path = os.path.join("ServerSide", "Keys", "private_key.pem")
    if os.path.getsize(server_private_key_path) == 0:
        with open(server_private_key_path, "wb") as file:
            file.write(private_key_pem)

    # Get the public key from the key pair
    public_key = private_key.public_key()

    # Serialize the public key to PEM format
    public_key_pem = public_key.public_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PublicFormat.SubjectPublicKeyInfo
    )

    # Write the public key to the file if it is empty
    server_public_key_path = settings.SERVER_PUBLIC_KEY
    if os.path.getsize(server_public_key_path) == 0:
        with open(server_public_key_path, "wb") as file:
            file.write(public_key_pem)

    # Read the public key from the file
    with open(server_public_key_path, "rb") as file:
        public_key_pem = file.read()

    # Deserialize the public key from PEM format
    recreated_public_key = serialization.load_pem_public_key(public_key_pem)

    # Verify if the recreated public key matches the original public key
    return public_key.public_bytes(encoding=serialization.Encoding.PEM, format=serialization.PublicFormat.SubjectPublicKeyInfo) == recreated_public_key.public_bytes(encoding=serialization.Encoding.PEM, format=serialization.PublicFormat.SubjectPublicKeyInfo)

'''def decrypt(encrypted_data):
    encrypted_bytes = base64.b64decode(encrypted_data)
    decrypted_bytes = private_key.decrypt(
        encrypted_bytes,
        padding.OAEP(
            mgf=padding.MGF1(algorithm=hashes.SHA256()),
            algorithm=hashes.SHA256(),
            label=None
        )
    )
    return decrypted_bytes.decode('utf-8')'''
def getPrivateKey(username):
    public_key_path = os.path.join(settings.KEYS_DIRECTORY, f"{username}_private_key.pem")
    with open(public_key_path, "rb") as file:
        private_key_data = file.read()
    # Deserialize the private key from PEM format
    recreated_private_key = serialization.load_pem_private_key(private_key_data, password=None, backend=default_backend())
    return recreated_private_key

def getPublicKey(public_key_pem):
    # Deserialize the public key from PEM format
    public_key = serialization.load_pem_public_key(public_key_pem.encode('utf-8'), backend=default_backend())
    return public_key

def getRawPublicKey(username):
    public_key_path = os.path.join(settings.KEYS_DIRECTORY, f"{username}_public_key.pem")
    with open(public_key_path, "rb") as file:
        public_key_pem = file.read().decode('utf-8')
    return public_key_pem

def decrypt(encrypted_data):
    server_private_key_path = os.path.join("ServerSide", "Keys", "private_key.pem")
    with open(server_private_key_path, "rb") as file:
        private_key_data = file.read()
    # Deserialize the private key from PEM format
    recreated_private_key = serialization.load_pem_private_key(private_key_data, password=None, backend=default_backend())

    # Decode the Base64-encoded encrypted content
    encrypted_content = base64.b64decode(encrypted_data)
    # Decrypt the content using the private key
    decrypted_content = recreated_private_key.decrypt(encrypted_content, padding.OAEP(mgf=padding.MGF1(algorithm=hashes.SHA256()), algorithm=hashes.SHA256(), label=None))
    print(decrypted_content)
    # Decode the decrypted content assuming it is in UTF-8 encoding
    decrypted_content_utf8 = decrypted_content.decode("utf-8")

    return decrypted_content_utf8

'''def encrypt(data):
    with open(settings.CLIENT_PUBLIC_KEY, "rb") as file:
        public_key_pem = file.read()

    # Deserialize the public key from PEM format
    recreated_public_key = serialization.load_pem_public_key(public_key_pem)

    # Encrypt the data using the public key
    encrypted_message = recreated_public_key.encrypt(
        data.encode('utf-8'),
        padding.OAEP(
            mgf=padding.MGF1(algorithm=hashes.SHA256()),
            algorithm=hashes.SHA256(),
            label=None
        )
    )
    return base64.b64encode(encrypted_message).decode('utf-8')'''

def encrypt(data, username):
    public_key_path = os.path.join(settings.KEYS_DIRECTORY, f"{username}_public_key.pem")
    with open(public_key_path, "rb") as file:
        public_key_pem = file.read()

    # Deserialize the public key from PEM format
    public_key = serialization.load_pem_public_key(public_key_pem, backend=default_backend())

    # Encode the data assuming it is in UTF-8 encoding
    encoded_data = data.encode("utf-8")

    # Encrypt the data using the public key
    encrypted_data = public_key.encrypt(encoded_data, padding.OAEP(mgf=padding.MGF1(algorithm=hashes.SHA256()), algorithm=hashes.SHA256(), label=None))

    # Encode the encrypted data as Base64
    encrypted_data_base64 = base64.b64encode(encrypted_data).decode("utf-8")

    return encrypted_data_base64


def encrypt_list(data_list, username):
    public_key_path = os.path.join(settings.KEYS_DIRECTORY, f"{username}_public_key.pem")
    with open(public_key_path, "r") as file:
        public_key_pem = file.read()

    # Deserialize the public key from PEM format
    public_key = serialization.load_pem_public_key(public_key_pem, backend=default_backend())

    # Encode the data assuming it is in UTF-8 encoding
    encoded_data = data.encode("utf-8")

    encrypted_list = []
    for data in data_list:
        # Encrypt each element in the list using the public key
        encrypted_message = public_key.encrypt(
            data.encode('utf-8'),
            padding.OAEP(
                mgf=padding.MGF1(algorithm=hashes.SHA256()),
                algorithm=hashes.SHA256(),
                label=None
            )
        )
        encrypted_list.append(base64.b64encode(encrypted_message).decode('utf-8'))

    return encrypted_list

def decrypt_list(encrypted_list):
    server_private_key_path = os.path.join("ServerSide", "Keys", "private_key.pem")
    with open(server_private_key_path, "rb") as file:
        private_key_pem = file.read()

    # Deserialize the private key from PEM format
    private_key = serialization.load_pem_private_key(private_key_pem, password=None, backend=default_backend())
    decrypted_list = []
    for encrypted_data in encrypted_list:
        encrypted_bytes = base64.b64decode(encrypted_data)
        decrypted_bytes = private_key.decrypt(
            encrypted_bytes,
            padding.OAEP(
                mgf=padding.MGF1(algorithm=hashes.SHA256()),
                algorithm=hashes.SHA256(),
                label=None
            )
        )
        decrypted_list.append(decrypted_bytes.decode('utf-8'))

    return decrypted_list