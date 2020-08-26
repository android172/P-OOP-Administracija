function init(){
    console.log(getCookie('subject_id'))
	makeRequest('/get_all_students_from_subject',[['subject_id',subjectID]],function(subjectData){loadAllStudents(allStudData);});
	makeRequest('/get_attempts_info_for_subject',[['subject_id',SubjectID]],function(gradeData){attemptsInfo(attemptsData);});
	makeRequest('/get_students_by_exam',[['exam_id',index]],function(studentData){getStudentsByExam(studExamData);});
}

function loadAllStudents(allStudData){
    console.log(allStudData);
}

function attemptsInfo(attemptsData){
    console.log(attemptsData);
}
