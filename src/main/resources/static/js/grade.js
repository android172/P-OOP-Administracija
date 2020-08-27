function init() {
    var subjectID = getCookie('subject_id');
    console.log(subjectID);
    makeRequest('/get_all_students_from_subject', [
        ['subject_id', subjectID]
    ], function(subjectData) { loadAllStudents(subjectData); });
    makeRequest('/get_attempts_info_for_subject', [
        ['subject_id', subjectID]
    ], function(gradeData) { attemptsInfo(gradeData); });
    makeRequest('/get_students_by_exam', [
        ['exam_id', index]
    ], function(studentData) { getStudentsByExam(studentData); });
}

function loadAllStudents(allStudData) {
    console.log(allStudData);
}

function attemptsInfo(attemptsData) {
    console.log(attemptsData);
}

function getStudentsByExam(examData) {
    console.log(examData);
}