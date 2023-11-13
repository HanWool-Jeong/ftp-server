function remove_child() {
    const parent = document.getElementById("file_list");

    while (parent.firstChild) {
        parent.firstChild.remove();
    }   
}

async function get_file_list(target_dir_id) {
    const parent = document.getElementById("file_list");
    const get_list_api = '/ftp/get_list?dirId=';
    const download_api = '/ftp/download?fileId=';

    const response = await (await fetch(get_list_api + target_dir_id)).json();

    // 부모 디렉토리 만들기
    if (response.parentDirectory) {
        let target_url = response.parentDirectory.id;
        let new_file_row = document.createElement("a");
        let tempDiv = document.createElement("div");

        new_file_row.setAttribute('href', "javascript:void(0)");
        new_file_row.addEventListener('click', () => refresh_file_list(target_url));
        new_file_row.appendChild(document.createTextNode("../"));

        tempDiv.appendChild(new_file_row);
        parent.appendChild(tempDiv);
    }

    // 현재 디렉토리 정보 표시
    const cur_dir = document.getElementById("cur_dir");
    cur_dir.innerHTML = "현재경로: " + response.curDirectory.name;
    cur_dir_id = target_dir_id;

    // 하위 디렉토리, 파일들 표시
    for (var i = 0; i < response.files.length; i++) {
        let new_file_row = document.createElement("a");

        if (response.files[i].directoryFlag == true) {   
            // 디렉토리일 때: 새 경로로 입장
            let target_url = response.files[i].id;
            new_file_row.setAttribute('href', "javascript:void(0)");
            new_file_row.addEventListener('click', () => refresh_file_list(target_url));
        } else {
            // 일반 파일일때: 그 파일 다운로드
            new_file_row.setAttribute('href', download_api + response.files[i].id);
            new_file_row.setAttribute('download', response.files[i].name);
        }

        new_file_row.appendChild(document.createTextNode(response.files[i].name));
        let tempDiv = document.createElement("div");
        tempDiv.appendChild(new_file_row);
        parent.appendChild(tempDiv);
    }
    // 변수 선언 var대신 let을 사용하자..
    // then 콜백함수보다 await를 쓰자..
}

function refresh_file_list(dirId) {
    remove_child();
    get_file_list(dirId);
}

function load_files() {
    const files = document.getElementById('upload_button').files;
    const submit_button = document.getElementById('submit_button');

    if (files.length > 0)
        submit_button.disabled = false;
    else
        submit_button.disabled = true;
}

async function upload_files() {
    const files = document.getElementById('upload_button').files;
    const formData = new FormData();

    for (let i = 0; i < files.length; i++)
        formData.append('files', files[i]);
    formData.append('dirId', cur_dir_id);
    
    const api = '/ftp/upload';
    const data = { method: "POST", body: formData };

    const response = await (await fetch(api, data)).json();

    if (response.ok == true) {
        alert('파일이 정상적으로 업로드 되었습니다.');
        refresh_file_list(cur_dir_id);
    } else {
        alert('파일 업로드 중 오류가 발생했습니다.');
    }
}