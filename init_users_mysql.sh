#!/bin/bash

# Database connection details
DB_HOST="localhost"
DB_PORT="3307"
DB_NAME="puresynth"
DB_USER="pUser"
DB_PASS="pSynth"

# Function to insert a user
insert_user() {
    local username="$1"
    local password="$2"
    local email="$3"
    local role="$4"

    local sql="
        INSERT INTO roles(name) VALUES ('$role') ON DUPLICATE KEY UPDATE name=name;
        INSERT INTO user(username, password, email) VALUES ('$username', '$password', '$email');
        SET @user_id = LAST_INSERT_ID();
        INSERT INTO user_roles(user_id, role_id) SELECT @user_id, id FROM roles WHERE name='$role';
        "

    echo "$sql" | mysql -h $DB_HOST -P $DB_PORT -u $DB_USER -p$DB_PASS $DB_NAME
}

# Insert users
insert_user "defaultUser" '$2a$10$0EdfHmYNAWxVE7yBn5haiuikairGn4zA3tmw/VvjcHSi2v5i0Naw' "defaultUser@mail.com" "ROLE_USER"
insert_user "adminUser" '$2a$10$D4XDXQC/XTC.ub0k/o0f3ufMVbnMHtcn8d6i5b9MtRfepXehVLiaC' "adminUser@mail.com" "ROLE_ADMIN"

echo "User initialization complete."
