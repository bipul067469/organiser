git clone <repository-url>

cd organiser

CREATE DATABASE postgres;

create tables in the database

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);


CREATE TABLE tasks (
                       id SERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       due_date DATE,
                       attachments TEXT,
                       status VARCHAR(50),
                       assignee_id BIGINT,
                       FOREIGN KEY (assignee_id) REFERENCES users(id)
);


CREATE TABLE comments (
                          id SERIAL PRIMARY KEY,
                          comment TEXT NOT NULL,
                          task_id BIGINT,
                          created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);


Update application.properties with your database credentials.

mvn clean install

mvn spring-boot:run

API Endpoints
-Create Task
POST /tasks

-Get Task by ID
GET /tasks/{id}

-Get Tasks by Assignee ID
GET /tasks/assignee/{assigneeId}

-Update Task
PUT /tasks/{id}

-Delete Task
DELETE /tasks/{id}

-Add Comment to Task
POST /tasks/{taskId}/comments
