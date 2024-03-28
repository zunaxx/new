insert into users(id, first_name, last_name, email, password, role)
values (1, 'Donald', 'Tramp', 'dony@gmail.com', '$2a$12$nRsrpMh3h7DOZt2bNj/sguCDr4C.noS4V09zIw9//Zr6kn8mb355C',
        'USER'),
       (2, 'Ilon', 'Mask', 'ilon@gmail.com', '$2a$12$CzMezh4xs3FJP8fqrwxmqusRkWEHoDI6afswwiXJhhV3A5kE00qv2',
        'USER');

insert into tests(id, title, description, enable, duration)
values (1, 'Test number 1', 'Test for first lesson', true, 240);

insert into questions(id, title, duration, enable, question_type, test_id)
values (1, 'Select the real english words', 60, true,
        'SELECT_REAL_ENGLISH_WORD', 1);

insert into questions(id, title, duration, enable, question_type, test_id)
values (2, 'Listen and select English words', 60, true,
        'LISTEN_AND_SELECT_ENGLISH_WORDS', 1);

insert into questions(id, title, duration, attempts, enable, file_url, question_type, test_id)
values (3, 'Write what you hear', 60, 2, true,
        'https://billingual-10.s3.eu-central-1.amazonaws.com/1703575411751art-in-our-life.mp3',
        'TYPE_WHAT_YOU_HEAR', 1);

insert into questions(id, title, duration, enable, file_url, question_type, correct_answer, test_id)
values (4, 'Describe the image', 60, true,
        'https://billingual-10.s3.eu-central-1.amazonaws.com/1703575547272cutes animal.jpg',
        'DESCRIBE_IMAGE',
        'One cute animal', 1);

insert into questions(id, title, duration, enable, statement, question_type, test_id)
values (5, 'Record yourself saying the statement below', 60, true, '
Literally I went and got lost',
        'RECORD_SAYING_STATEMENT', 1);

insert into questions(id, title, duration, enable, statement, question_type, attempts, test_id)
values (6, 'Respond in at least N words', 60, true, 'Describe a time you were surprised, what happened?',
        'RESPOND_AT_LEAST_N_WORDS', 30, 1);

insert into questions(id, title, duration, enable, passage, statement, question_type, correct_answer, test_id)
values (7, 'Highlight the answer', 60, true, 'What did resident think could happen with new bridge',
        'One of statement in question with id: 7 highlight the answer ', 'HIGHLIGHT_THE_ANSWER',
        'correct answer is highlight the answer', 1);

insert into questions(id, title, duration, enable, passage, question_type, test_id)
values (8, 'Select the main idea in this options', 60, true, 'In a literal sense, a snowman is a figure made of snow that is shaped to resemble a person. It is a popular winter activity, especially during the holiday season, to build snowmen. These are often adorned with accessories like scarves, hats, and buttons.The Snowman (Raymond Briggs): "The Snowman" is also the title of a popular children''s picture book by Raymond Briggs. It was later adapted into an animated television film. The story is about a boy who builds a snowman that comes to life and takes him on a magical journey.',
        'SELECT_THE_MAIN_IDEA', 1),
       (9, 'Select the best title in this options', 60, true, 'The story follows Kevin McCallister, an 8-year-old boy played by Macaulay Culkin, who is accidentally left behind when his family goes on vacation for Christmas. Kevin initially revels in the freedom of having the house to himself but soon has to defend it from burglars Harry and Marv, played by Joe Pesci and Daniel Stern, who are attempting to rob houses in the neighborhood.e',
        'SELECT_THE_BEST_TITLE', 1);

insert into options(id, title, is_true, audio_url, question_id)
values (1, 'family', true, null,1),
       (2, 'weord', false,null,1),
       (3, 'chroome', false,null,1),
       (4, 'swagger', true,null,1),
       (5, 'tiam', false, null,1),
       (6, 'word 1', true, 'https://billingual-10.s3.eu-central-1.amazonaws.com/1701946842008ashnikko_-_daisy.mp3',2),
       (7, 'word 2', false, 'https://billingual-10.s3.eu-central-1.amazonaws.com/1701946842008ashnikko_-_daisy.mp3',2),
       (8, 'word 3', true, 'https://billingual-10.s3.eu-central-1.amazonaws.com/1701946842008ashnikko_-_daisy.mp3',2),
       (9, 'word 4', true, 'https://billingual-10.s3.eu-central-1.amazonaws.com/1701946842008ashnikko_-_daisy.mp3',2),
       (10,'word 5', false,'https://billingual-10.s3.eu-central-1.amazonaws.com/1701946842008ashnikko_-_daisy.mp3',2),
       (11,'it is one of men in winter',true,null,8),
       (12,'these are snowdrifts on the street ',false,null,8),
       (13,'This is hot coffee with marshmallows',false,null,8),
       (14,'it''s a snowman outside',false,null,8),
       (15,'STBT first option',true,null,9),
       (16,'STBT second option',false,null,9),
       (17,'STBT third option',false,null,9),
       (18,'STBT forth option',false,null,9);

insert into answers(id,statement,audio_file,question_type,user_id,question_id)
values (1,null, null, 'SELECT_REAL_ENGLISH_WORD', 1,1),
       (2,null, null, 'LISTEN_AND_SELECT_ENGLISH_WORDS', 1,2),
       (3,'Correct answer for question Type what you hear',null,'TYPE_WHAT_YOU_HEAR',1,3),
       (4,'Correct answer for question Describe image', null, 'DESCRIBE_IMAGE',1,4),
       (5,null, 'https://billingual-10.s3.eu-central-1.amazonaws.com/1699881836884drive-breakbeat-173062.mp3','RECORD_SAYING_STATEMENT',1,5),
       (6,'Respond in at least N words statement dont worry',null,'RESPOND_AT_LEAST_N_WORDS',1,6),
       (7,'Highlighted statement in text in this question but its not end',null,'HIGHLIGHT_THE_ANSWER',1,7),
       (8,null,null,'SELECT_THE_MAIN_IDEA',1,8),
       (9,null,null,'SELECT_THE_BEST_TITLE',1,9);

insert into options_users(options_id, users_id)
values (1,1),
 (4,1),
 (6,1),
 (8,1),
 (9,1),
 (11,1),
 (3,1),
 (2,1),
 (1,1),
 (15,1);


insert into results(id, date_of_submission, checked,  score, answer_id)
values (1, '2023-12-26 07:30:23.244153 +00:00',true,10, 1),
       (2, '2023-12-26 07:30:23.244153 +00:00', true,  10,  2),
       (3, '2023-12-26 07:30:23.244153 +00:00', true,  10, 3),
       (4, '2023-12-26 07:30:23.244153 +00:00', true,  10,  4),
        (5, '2023-12-26 07:30:23.244153 +00:00', true,  10,  5),
        (6, '2023-12-26 07:30:23.244153 +00:00', true,  10,  6),
        (7, '2023-12-26 07:30:23.244153 +00:00', true,  10,  7),
         (8, '2023-12-26 07:30:23.244153 +00:00', true, 10, 8),
          (9, '2023-12-26 07:30:23.244153 +00:00', true,  10,  9);

insert into users_tests(users_id, tests_id)
values (1, 1);