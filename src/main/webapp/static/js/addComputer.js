/*


$(document).submit(function(event) {
	var introduced = $("#introduced").val();
	var discontinued = $("#discontinued").val();
	console.log("log : "+introduced);
	console.log( introduced.length >1);
	
	console.log("log : "+discontinued);
	console.log( discontinued.length );
	
	if (introduced.length > 1) {
		if (discontinued.length > 1) {
			if (new Date(introduced) > new Date(discontinued)) {
			document.getElementById("erroradd").innerHTML =
			"Date Interval not Valid ";
				event.preventDefault();
				$("#alert-message").show();
				console.log( $("#validationForm").val() );
				this.setAttribute("validationForm","false")
				console.log( $("#validationForm").val() );
			}
		}
	}
	var name = $("#computerName").val();
	
	if (name == "null" | name.startsWith(" ")){
	
	
	document.getElementById("erroradd").innerHTML =
	"Computer Name not Valid ";
		event.preventDefault();
		$("#alert-message").show();
		console.log( $("#validationForm").val() );
		this.setAttribute("validationForm","false")
		console.log( $("#validationForm").val() );
	}	
	
});



$(function() {
  $("form[name='addComputerForm']").validate({
    rules: {
      computerName: "required",
	  introduced: {
		date : true, 
	  },
	  discontinued : {
		date: true,
		greaterThan:  "#introduced",
	  },
    },
    messages: {
      computerName: "Name is required !",
	  introduced: "Please enter a valid date",
	  discontinued : {
		date: "Please enter a valid date",
		greaterThan: "Discontinued date must be greater than introduced date"
		}
    },
    submitHandler: function(form) {
      form.submit();
    }
  });
});


jQuery.validator.addMethod("greaterThan", function(value, element, params) {
    if (!/Invalid|NaN/.test(new Date(value)) && !/Invalid|NaN/.test(new Date($(params).val()))){
        return new Date(value) > new Date($(params).val());
    }
	else if (!/Invalid|NaN/.test(new Date(value)) && /Invalid|NaN/.test(new Date($(params).val()))){
		return false;
	}
    return true; 
});




let inputDiscontinued = $("#discontinued")
let inputIntroduced = $("#introduced")
let submitBtn = $("#submitBtn")


// add listener
inputDiscontinued.change(function() {
	checkDiscontinuedIfNotIntroduced();
});

inputIntroduced.change(function() {
	checkDiscontinuedIfNotIntroduced();
});


function checkDiscontinuedIfNotIntroduced() {
	let errorTag = $("#errorDiscontinued")
	console.log(inputIntroduced.val())
	if (inputIntroduced.val() === "" &&  inputDiscontinued.val() !== "") {
		errorTag.html("The introduced date cannot be empty if the discontinued is it.")
		errorTag.addClass("alert alert-danger")
	} else {
		errorTag.removeClass()
		errorTag.html("")
	}
}

*/

console.log("t")
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



