from cryptography.hazmat.primitives.asymmetric import rsa,padding
from cryptography.hazmat.primitives import serialization,hashes
from django.conf import settings
import base64
import os
from cryptography.hazmat.backends import default_backend
from ServerSide import securityHandler

private_key = None

def generateKeys():
    global private_key
    # Generate a new RSA key pair
    private_key = rsa.generate_private_key(
        public_exponent=65537,
        key_size=2048
    )

    # Get the public key from the key pair
    public_key = private_key.public_key()

    # Serialize the public key to PEM format
    public_key_pem = public_key.public_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PublicFormat.SubjectPublicKeyInfo
    )

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
def decrypt(encrypted_data, username):
    encrypted_content_base64 = base64.b64decode(encrypted_data)
    private_key_path = os.path.join(settings.KEYS_DIRECTORY, f"{username}_private_key.pem")
    with open(private_key_path, "rb") as file:
        env_data = file.read()

    # Deserialize the private key from PEM format
    recreated_private_key = serialization.load_pem_private_key(env_data, password=None, backend=default_backend())

    # Decode the Base64-encoded encrypted content

    # Decrypt the content using the private key
    decrypted_content = recreated_private_key.decrypt(encrypted_content_base64, padding.OAEP(mgf=padding.MGF1(algorithm=hashes.SHA256()), algorithm=hashes.SHA256(), label=None))

    # Decode the decrypted content assuming it is in UTF-8 encoding
    decrypted_content_utf8 = decrypted_content.decode("utf-8")

    return decrypted_content_utf8

def encrypt(data):
    with open(settings.SERVER_PUBLIC_KEY, "rb") as file:
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
    return base64.b64encode(encrypted_message).decode('utf-8')

def encrypt_list(data_list):
    with open(settings.SERVER_PUBLIC_KEY, "rb") as file:
        public_key_pem = file.read()

    # Deserialize the public key from PEM format
    recreated_public_key = serialization.load_pem_public_key(public_key_pem)

    encrypted_list = []
    for data in data_list:
        # Encrypt each element in the list using the public key
        encrypted_message = recreated_public_key.encrypt(
            data.encode('utf-8'),
            padding.OAEP(
                mgf=padding.MGF1(algorithm=hashes.SHA256()),
                algorithm=hashes.SHA256(),
                label=None
            )
        )
        encrypted_list.append(base64.b64encode(encrypted_message).decode('utf-8'))

    return encrypted_list

def decrypt_list(encrypted_list,username):
    encrypted_content_base64 = base64.b64decode(encrypted_data)
    private_key_path = os.path.join(settings.KEYS_DIRECTORY, f"{username}_private_key.pem")
    with open(private_key_path, "r") as file:
        env_data = file.read()

    # Deserialize the private key from PEM format
    recreated_private_key = serialization.load_pem_private_key(env_data.encode(), password=None, backend=default_backend())
    decrypted_list = []
    for encrypted_data in encrypted_list:
        encrypted_bytes = base64.b64decode(encrypted_data)
        decrypted_bytes = recreated_private_key.decrypt(
            encrypted_bytes,
            padding.OAEP(
                mgf=padding.MGF1(algorithm=hashes.SHA256()),
                algorithm=hashes.SHA256(),
                label=None
            )
        )
        decrypted_list.append(decrypted_bytes.decode('utf-8'))

    return decrypted_list

def decrypt_record(encrypted_record,username):
    encrypted_name = encrypted_record.record_name
    encrypted_content = encrypted_record.record_content
    decrypted_name = decrypt(encrypted_name,username)
    decrypted_content = decrypt(encrypted_content,username)
    encrypted_record.record_name = decrypted_name
    encrypted_record.record_content = decrypted_content
    return encrypted_record

def decrypt_records(records,key_path):
    for encrypted_record in records:
        decrypt_record(encrypted_record,key_path)
    return records

def encrypt_file(file):
    file_name = file.name
    file_content = file.read().decode('utf-8')
    return encrypt(file_name), encrypt(file_content)

def decrypt_managment_requests(managment_requests,key_path):
    for request in  managment_requests:
        request.record = decrypt_record(request.record,key_path)
    return managment_requests

def generateKeysUser(username):

    private_key = rsa.generate_private_key(
        public_exponent=65537,
        key_size=2048
    )
    
    # Get the public key from the key pair
    public_key = private_key.public_key()

    # Create the directory if it doesn't exist
    os.makedirs(settings.KEYS_DIRECTORY, exist_ok=True)
    private_key_pem = private_key.private_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PrivateFormat.PKCS8,
        encryption_algorithm=serialization.NoEncryption()
    )
    private_key_path = os.path.join(settings.KEYS_DIRECTORY, f"{username}_private_key.pem")
    # Write private key to file
    with open(private_key_path, "w") as private_key_file:
        private_key_file.write(private_key_pem.decode())
    public_key_path = os.path.join(settings.KEYS_DIRECTORY, f"{username}_public_key.pem")
    # Write public key to file
    public_key_pem = public_key.public_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PublicFormat.SubjectPublicKeyInfo
    )
    with open(public_key_path, "w") as public_key_file:
        public_key_file.write(public_key_pem.decode())

    return "Keys generated succesfully"