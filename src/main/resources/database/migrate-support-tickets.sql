CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO support_tickets (uuid, user_id, subject, msg, is_mail_sent_to_user, is_mail_sent_to_admin, created_at) VALUES (uuid_generate_v4(), '1bd484ed-b5ab-4f88-b57e-3c167d53ecb2', 'Angry Subject', 'A really long and boring message.......', TRUE, TRUE, TIMESTAMP '2017-01-01 16:00:00');