function loadLecturerData(lecturerData) {
    var ime = lecturerData["firstName"];
    var prezime = lecturerData["lastName"];
    document.getElementById("Ime").innerHTML = ime;
    document.getElementById("Prezime").innerHTML = prezime;
    document.getElementById("Ime1").innerHTML = ime;
    document.getElementById("Prezime1").innerHTML = prezime;
    var title = lecturerData["title"];
    document.getElementById("Titula").innerHTML = title;
}

var subjects = {};

function loadLecturerSubjects(lecturerSubData) {
    var table2 = document.getElementById("table2");
    let table = '<table>'
    table += '<tr><th colspan = "6">Predmeti</th></tr>'
    table += '<tr><th>Predmet</th><th>IDPredmeta</th><th>Godina</th><th>ESPB</th><th>Smer</th></tr>'
    for (let i = 0; i < lecturerSubData.length; i++) {
        const subjectName = lecturerSubData[i]["subjectName"];
        const ID = lecturerSubData[i]["subjectId"];
        const espb = lecturerSubData[i]["espb"];
        const year = lecturerSubData[i]["year"];
        const majorName = lecturerSubData[i]["majorName"];
        subjects[ID] = subjectName;
        table += `<tr><td>${subjectName}</td><td>${ID}</td><td>${year}</td><td>${espb}</td><td>${majorName}</td></tr>`
    }
    table += '</table>'
    table2.innerHTML = table;
}

function loadExamDeadline(examData) {
    var table3 = document.getElementById("table3");
    let table = '<table>'
    var subjectName;
    table += '<tr><th colspan = "4">Ispiti</th></tr>'
    table += '<tr><th>Predmet</th><th>IDIspita</th><th>Datum</th><th>Ocenjivanje</th></tr>'
    for (let i = 0; i < examData.length; i++) {
        const subjectId = examData[i]["subject_id"];
        for (let j = 0; j < subjects.length; j++)
            if (subjectId === subjects[j])
                subjectName = examData[i]["subjectName"];
        const Id = examData[i]["id"];
        const date = examData[i]["date"];
        table += `<tr><td>${subjectName}</td><td>${Id}</td><td>${date}</td><td><a onclick = "redirectToGrade('${subjectId}')" id = "grade">Oceni</a></td></tr>`
    }
    table += '</table>'
    table3.innerHTML = table;
}

function redirectToGrade(subjectId) {
    console.log('=================')
    console.log(subjectId);
    setCookie("subject_id", subjectId);
    window.location.replace('/grading');
}

function init() {
    makeRequest('/get_lecturer', [
        ['lectId', index]
    ], function(lecturerData) { loadLecturerData(lecturerData); });
    makeRequest('/get_subjects_by_lecturer', [
        ['lect_id', index]
    ], function(lecturerSubData) { loadLecturerSubjects(lecturerSubData); });
    makeRequest('get_exams_for_lecturer', [
        ['lecturer_id', index]
    ], function(examData) { loadExamDeadline(examData); });
}