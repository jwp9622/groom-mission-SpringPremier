
const API_URL = '/auth'; // 주소 수정 필요


document.addEventListener("DOMContentLoaded", function() {

    //회원 가입
    const signBtn = document.querySelector("#sign_btn");
    if(signBtn){
        signBtn.addEventListener("click", function() {

            const form = document.getElementById("sign_form");

            if( form.memberId.value === ""){
                alert("아이디를 입력하세요"); form.memberId.focus(); return false;
            }
            if( form.password.value === ""){
                alert("비밀번호를 입력하세요"); form.password.focus();  return false;
            }
            if( form.confirm_password.value === ""){
                alert("비밀번호가 입력하세요."); form.confirm_password.focus(); return false;
            }
            if( form.password.value !== form.confirm_password.value){
                alert("비밀번호가 일치하여야합니다."); form.confirm_password.focus();  return false;
            }
            if( form.email.value === ""){
                alert("이메일을 입력하세요"); form.email.focus();  return false;
            }
            if( form.username.value === ""){
                alert("이름을 입력하세요"); form.username.focus();  return false;
            }
            if( form.chkAgree.checked === false){
                alert("약관에 동의하셔아합니다."); form.chkAgree.focus();  return false;
            }

            //form 데이터값 가져오기
            const formObj = new FormData(form);
            const formdata = {};
            formObj.forEach((value,key) =>{
                formdata[key] = value;
            });


            // REST API 호출
            const url = API_URL+"/signup";
            fetch(url, {
                method: "POST",     // post 요청
                headers: {           // 전송 데이터 타입(JSON) 정보
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(formdata) // JSON 문자열로 변환하여 전송
            })
            .then(res => {
                //console.log("res==>"+res);
                return res.json();
            })
            .then(res => {

                const invalidElements = document.querySelectorAll(".invalid");
                invalidElements.forEach(element => {
                    element.textContent = "";
                });


                for (const key in res) {
                    if (res.hasOwnProperty(key)) { // 객체의 속성인지 확인 (상속받은 속성 제외)
                        console.log(key +"__"+res[key]);
                    }
                }
                if(res.success == true){
                    alert('회원가입 성공히였습니다.');
                    window.location.href = '/';
                }else{
                    for(var key in res.data) {
                      const obj = document.getElementById(key);
                      if(obj) obj.textContent = res.data[key];
                    }

                }
            })
            .catch(error => {
                // 네트워크 오류 또는 위에서 던진 에러 처리
                console.error('요청 실패:', error);
            });

        });
    }


    //로그인
    const loginBtn = document.querySelector("#login_btn");
    if(loginBtn){

        loginBtn.addEventListener("click", function() {
          const form = document.querySelector("#login_form");
          const username = form.username.value;
          const password = form.password.value;

            //form 데이터값 가져오기
            const formObj = new FormData(form);
            const formdata = {};
            formObj.forEach((value,key) =>{
                formdata[key] = value;
            });

            if( form.username.value == ""){
                alert("아이디를 입력하세요"); form.username.focus();  return false;
            }

            if( form.password.value == ""){
                alert("비밀번호를 입력하세요"); form.password.focus();  return false;
            }

          const url = API_URL+"/login";
          fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formdata)
          })
          .then(res => {
            //console.log(res);
            return res.json();
          })
          .then(res => {
            /*
            console.log("res=="+res);
             for (const key in res) {
                    if (res.hasOwnProperty(key)) { // 객체의 속성인지 확인 (상속받은 속성 제외)
                        console.log(key +"__"+res[key]);
                    }
             }
             */

            if(!res.success){
                alert('로그인이 실패하였습니다.');
                document.querySelector("#message").textContent = res.message;
            }else{
                alert('로그인 되었습니다.');
                localStorage.setItem('access_token', res.data.accessToken);
                localStorage.setItem('refresh_token', res.data.refreshToken);
                document.querySelector("#message").textContent = "";

                //console.log("access_token="+res.data.accessToken);
                //console.log("refresh_token="+res.data.refreshToken);

                // 로그인 성공 후 보호된 정보 호출 (예: 사용자 정보, 권한 등)
                const userInfo = getProtectedResource();

                //console.log("localStorage="+localStorage.getItem('access_token'));
                //headers: 'Authorization': 'Bearer ' + res.data.accessToken;
                parent.location.href="/";

            }
          })
          .catch(error => {
                // 네트워크 오류 또는 위에서 던진 에러 처리
                console.error('요청 실패:', error);
           })

        });
    }

});
///////////////////////////////////////////////////////
    //로그인 확인

    async function getProtectedResource() {
      const token = localStorage.getItem('access_token');

        //console.log("protected==="+token);
      const response = await fetch('/auth/protected', {
        method: 'GET',
        headers: {
          'Authorization': 'Bearer '+token
        }
      });

        //console.log("response======"+response);
        //console.log("response.ok======"+response.ok);

      if (response.ok) {
        const data = await response.json();
        return data;
      } else if (response.status === 401) {
        throw new Error('Unauthorized: 토큰이 유효하지 않음');
      } else {
        throw new Error('요청 실패');
      }
    }

////////////////////////////////////////////////////////

    //logout
    function logout() {
        console.log("accessToken==>" + localStorage.getItem('access_token'));

        // 로그아웃 요청
        fetch('/auth/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('access_token')
            }
        })
            .then(res => res.json())
            .then(res => {
                if (!res.success) {
                    alert("false=" + res.message);
                } else {
                    alert('로그아웃 되었습니다.');
                    localStorage.removeItem('accessToken');
                    localStorage.removeItem('refreshToken');
                    window.location.href = '/';
                }
            });


        //로그인 상태 유지
        async function loadUserInfo() {
            const res = await fetchWithAuth('/api/user/info');
            if (res.ok) {
                const data = await res.json();
                document.getElementById('username').innerText = data.username;
            }
        }

        //토큰 재발급
        async function reissueToken() {
            const refreshToken = localStorage.getItem('refresh_token');

            if (!refreshToken) {
                alert('Refresh Token이 없습니다. 다시 로그인해주세요.');
                return;
            }

            try {
                const response = await fetch('/api/reissue', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        refreshToken: refreshToken
                    })
                });

                if (response.ok) {
                    const data = await response.json();
                    localStorage.setItem('accessToken', data.accessToken);
                    localStorage.setItem('refreshToken', data.refreshToken); // 필요 시
                    alert('토큰 재발급 성공!');
                } else {
                    alert('토큰 재발급 실패: 다시 로그인 해주세요.');
                    window.location.href = '/login';
                }
            } catch (error) {
                console.error('토큰 재발급 중 오류 발생:', error);
            }
        }


        //토큰 만료 여부 체크
        async function checkAuthAndFetchUserInfo() {
            const accessToken = localStorage.getItem("access_token");

            try {
                const response = await fetch("/api/user/info", {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                });

                if (response.status === 401) {
                    // 액세스 토큰이 만료되었으면 refresh 토큰으로 재발급 시도
                    await reissueToken();

                    // 재발급 후 다시 요청
                    const retryResponse = await fetch("/api/user/info", {
                        method: "GET",
                        headers: {
                            Authorization: `Bearer ${localStorage.getItem("access_token")}`
                        }
                    });

                    if (retryResponse.ok) {
                        const data = await retryResponse.json();
                        console.log("재요청 결과:", data);
                    } else {
                        console.error("재발급 후 요청 실패");
                    }

                } else if (response.ok) {
                    const data = await response.json();
                    console.log("유저 정보:", data);
                } else {
                    console.error("예상치 못한 에러:", response.status);
                }

            } catch (err) {
                console.error("요청 실패:", err);
            }
        }
    }