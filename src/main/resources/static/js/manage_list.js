function remove_child() {
    const parent = document.getElementById("file_list");

    while (parent.firstChild) {
        parent.firstChild.remove();
    }   
}

function get_file_list(path) {
    const parent = document.getElementById("file_list");

    fetch(path)
        .then(res => {
            if (res.status === 200)
                return res.json();
            else
                console.log("failed!!");
        })
        .then(json_data => {
            // 부모 디렉토리 만들기
            if (json_data.parentDirectory) {
                alert('hi');
                let target_url = "/ftp/get_list?dirId=" + json_data.parentDirectory.id;
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
            cur_dir.innerHTML = "현재경로: " + json_data.curDirectory.name;

            // 하위 디렉토리, 파일들 표시
            for (var i = 0; i < json_data.files.length; i++) {
                let new_file_row = document.createElement("a");

                if (json_data.files[i].directoryFlag == true) {   
                    // 디렉토리일 때
                    let target_url = "/ftp/get_list?dirId=" + json_data.files[i].id;
                    new_file_row.setAttribute('href', "javascript:void(0)");
                    new_file_row.addEventListener('click', () => refresh_file_list(target_url));
                } else {
                    // 일반 파일일때
                    new_file_row.setAttribute('href', "/ftp/download?fileId=" + json_data.files[i].id);
                    new_file_row.setAttribute('download', json_data.files[i].name);
                }

                new_file_row.appendChild(document.createTextNode(json_data.files[i].name));
                let tempDiv = document.createElement("div");
                tempDiv.appendChild(new_file_row);
                parent.appendChild(tempDiv);
            }
        });
    // 변수 선언 var대신 let을 사용하자..
}

function refresh_file_list(path) {
    remove_child();
    get_file_list(path);
}