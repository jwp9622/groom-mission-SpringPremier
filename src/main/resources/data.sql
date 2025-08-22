


--admin/1234    $2a$10$i1A8fhv9bo24eVNdU.ENeO.AE7kVlxYvhwRKz4NLFXoJw0UZLOvnC
--aaaa/1111    $2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq
--bbbb/2222    $2a$10$pX.uNsLWcVg7ZYoVG1f6E.ntcfyovpsEVlGjos8BPMRpHni0bLuXy
--cccc/3333    $2a$10$/XijRgIqUBqcTuz.tvvmZOwLyRVulviKvQEPIuptgzR.xGLG8K8Ci
--dddd/4444    $2a$10$xNWUJuJYJPWF93byVkHeVeQgItrNn9kkMRGa3Vy785EVbxWqXJG8S

INSERT INTO Member(memberId, role, password, email, username, createdAt,updatedAt, expiredAt) VALUES('admin', 'ROLE_ADMIN', '$2a$10$i1A8fhv9bo24eVNdU.ENeO.AE7kVlxYvhwRKz4NLFXoJw0UZLOvnC', 'admin@naver.com','관리자','2025-05-01', null, null);
INSERT INTO Member(memberId, role, password, email, username, createdAt,updatedAt, expiredAt) VALUES('aaaa', 'ROLE_USER', '$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq', 'aaaa@naver.com','사용자a','2025-05-02', null, null);
INSERT INTO Member(memberId, role, password, email, username, createdAt,updatedAt, expiredAt) VALUES('bbbb', 'ROLE_USER', '$2a$10$pX.uNsLWcVg7ZYoVG1f6E.ntcfyovpsEVlGjos8BPMRpHni0bLuXy', 'bbbb@naver.com','사용자b','2025-05-03', null, null);
INSERT INTO Member(memberId, role, password, email, username, createdAt,updatedAt, expiredAt) VALUES('cccc', 'ROLE_USER', '$2a$10$/XijRgIqUBqcTuz.tvvmZOwLyRVulviKvQEPIuptgzR.xGLG8K8Ci', 'cccc@naver.com','사용자c','2025-05-04', null, null);
INSERT INTO Member(memberId, role, password, email, username, createdAt,updatedAt, expiredAt) VALUES('dddd', 'ROLE_USER', '$2a$10$xNWUJuJYJPWF93byVkHeVeQgItrNn9kkMRGa3Vy785EVbxWqXJG8S', 'dddd@naver.com','사용자d','2025-05-05', null, null);



-- 게시판 관리자
INSERT INTO Board(  code, name, description, pageSize, createdAt, updatedAt) VALUES('notice', '공지사항', '공지사항게시판입니다.',10,'2025-06-06 00:00:00', null);
INSERT INTO Board(  code, name, description, pageSize, createdAt, updatedAt) VALUES( 'qna', 'QnA', 'QnA게시판입니다.',10,'2025-06-05', null);
INSERT INTO Board(  code, name, description, pageSize, createdAt, updatedAt) VALUES( 'list', 'QnA', 'QnA게시판입니다.',10,'2025-06-05', null);

INSERT INTO Board(  code, name, description, pageSize, createdAt, updatedAt) VALUES( 'calendar', 'board1', 'board1 게시판입니다.',10,'2025-06-05', null);
INSERT INTO Board(  code, name, description, pageSize, createdAt, updatedAt) VALUES( 'poll', 'board2', 'board2 게시판입니다.',10,'2025-06-05', null);

INSERT INTO Board(  code, name, description, pageSize, createdAt, updatedAt) VALUES( 'blog', 'board3', 'board3 게시판입니다.',10,'2025-06-05', null);
INSERT INTO Board(  code, name, description, pageSize, createdAt, updatedAt) VALUES( 'board4', 'board4', 'board4 게시판입니다.',10,'2025-06-05', null);
INSERT INTO Board(  code, name, description, pageSize, createdAt, updatedAt) VALUES( 'board5', 'board5', 'board5 게시판입니다.',10,'2025-06-05', null);

INSERT INTO Board(  code, name, description, pageSize, createdAt, updatedAt) VALUES( 'board6', 'board6', 'board6 게시판입니다.',10,'2025-06-05', null);
INSERT INTO Board(  code, name, description, pageSize, createdAt, updatedAt) VALUES( 'board7', 'board7', 'board7 게시판입니다.',10,'2025-06-05', null);
INSERT INTO Board(  code, name, description, pageSize, createdAt, updatedAt) VALUES( 'board8', 'board8', 'board8 게시판입니다.',10,'2025-06-05', null);
INSERT INTO Board(  code, name, description, pageSize, createdAt, updatedAt) VALUES( 'board9', 'board9', 'board9 게시판입니다.',10,'2025-06-05', null);
INSERT INTO Board(  code, name, description, pageSize, createdAt, updatedAt) VALUES( 'board10', 'board10', 'board10 게시판입니다.',10,'2025-06-05', null);


--게시판 테이블
INSERT INTO Post(ref, step, seq, board_code, member_memberId, title, name, content, password, createdAt, updatedAt) VALUES( 1, 0, 0, 'notice',  'admin', '공지사항1', '관리자', '1내용입니다.. 내용입니다.','$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq','2025-05-01', null);
INSERT INTO Post(ref, step, seq, board_code, member_memberId, title, name, content, password, createdAt, updatedAt) VALUES( 2, 0, 0, 'notice',  'admin', '공지사항2', '관리자', '2내용입니다.. 내용입니다.','$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq','2025-05-02', null);
INSERT INTO Post(ref, step, seq, board_code, member_memberId, title, name, content, password, createdAt, updatedAt) VALUES( 3, 0, 0, 'notice',  'admin', '공지사항3', '관리자', '3내용입니다.. 내용입니다.','$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq','2025-05-03', null);
INSERT INTO Post(ref, step, seq, board_code, member_memberId, title, name, content, password, createdAt, updatedAt) VALUES( 4, 0, 0, 'qna',     'aaaa', 'qna1', '사용자', 'qna1내용입니다.. 내용입니다.','$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq','2025-05-04', null);
INSERT INTO Post(ref, step, seq, board_code, member_memberId, title, name, content, password, createdAt, updatedAt) VALUES( 5, 0, 0, 'qna',     'aaaa', 'qna2', 'aaaa', 'qna2내용입니다.. 내용입니다.','$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq','2025-05-05', null);
INSERT INTO Post(ref, step, seq, board_code, member_memberId, title, name, content, password, createdAt, updatedAt) VALUES( 6, 0, 0, 'qna',     'aaaa', 'qna3', 'bbbb', 'qna3내용입니다.. 내용입니다.','$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq','2025-05-06', null);


-- 코멘트 테이블
INSERT INTO Comment(post_id, member_memberId, nickname, content, password, createdAt, updatedAt) VALUES(3, 'aaaa', '사용자a', '11코멘트 내용입니다... 내용입니다.','$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq', '2025-05-06', null);
INSERT INTO Comment(post_id, member_memberId, nickname, content, password, createdAt, updatedAt) VALUES(3, 'bbbb', '사용자a', '22코멘트 내용입니다... 내용입니다.','$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq', '2025-05-06',  null);
INSERT INTO Comment(post_id, member_memberId, nickname, content, password, createdAt, updatedAt) VALUES(3, 'bbbb', '사용자a', '33코멘트 내용입니다... 내용입니다.', '$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq', '2025-05-06', null);
INSERT INTO Comment(post_id, member_memberId, nickname, content, password, createdAt, updatedAt) VALUES(3, null, '사용자a', '44코멘트 내용입니다... 내용입니다.','$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq', '2025-05-06',  null);
INSERT INTO Comment(post_id, member_memberId, nickname, content, password, createdAt, updatedAt) VALUES(3, null, '사용자a', '55코멘트 내용입니다... 내용입니다.','$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq', '2025-05-06', null);

