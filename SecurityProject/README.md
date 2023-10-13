# Developpers :
- Taha Ahmed 58744
- Lecky-Thompson William 58412

# Get Started :
To start using the project, follow these simple instructions:

1. Create a virtual environment :
```sh
python -m venv .venv
```
2. Activate the virtual environment :
   1. Windows :
    ```sh
    ./.venv/Scripts/activate
    ```
   2. Linux :
    ```sh
    source ./.venv/bin/activate
    ```
3. Enter the project's root directory :
```sh
cd Hospital/
```

4. Install the project's dependencies :
```sh
pip install -r requirements.txt
```

PS: If you are on linux and you are encountering an error talking about `pyCairo`, do the following then reattempt step 4:
```sh
sudo apt install libcairo2-dev pkg-config python3-dev
```
```sh
pip install pycairo
```
5. Migrate :
```sh
python manage.py makemigrations
```
```sh
python manage.py migrate
```

6. Start the project :
```sh
python manage.py runsslserver --cert Hospital/Keys/cert.pem --key Hospital/Keys/key.pem
```

You're now good to go!

# Important Notes:

- A default admin account is present. His username and password are `admin`.

- For the time being, **no email** is actually sent for 2FA, instead, the code is printed into the **project's console**.