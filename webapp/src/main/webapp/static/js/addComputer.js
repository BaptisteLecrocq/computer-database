

let inputIntroduced = $("#introduced")
let inputDiscontinued = $("#discontinued")

inputDiscontinued.change(function() {
	checkDiscontinuedIfNotIntroduced();
});

function checkDiscontinuedIfNotIntroduced() {
	console.log("test")
	let errorTag = $("#errorDiscontinued")
	console.log(inputIntroduced.val())
	if (inputIntroduced.val() === "" &&  inputDiscontinued.val() !== "") {
		errorTag.html("The introduced date cannot be empty if the discontinued isn't")
		errorTag.addClass("alert alert-danger")
	} else {
		errorTag.removeClass()
		errorTag.html("")
	}
}



