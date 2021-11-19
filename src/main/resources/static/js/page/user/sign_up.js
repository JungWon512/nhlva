
// Class definition
var KTWizardDemo = function () {
    // Base elements
    var wizardEl;
    var formEl;
    var validator;
    var wizard;

    // Private functions
    var initWizard = function () {
        // Initialize form wizard
        wizard = new KTWizard('kt_wizard_v4', {
            startStep: 1
        });

        // Validation before going to next page
        wizard.on('beforeNext', function(wizardObj) {
            if (validator.form() !== true) {
                wizardObj.stop();  // don't go to the next step
            }
        });

        // Change event
        wizard.on('change', function(wizard) {
            KTUtil.scrollTop();
        });
    };

    var initValidation = function() {
        validator = formEl.validate({
            // Validate only visible fields
            ignore: ":hidden",

            // Validation rules
            rules: {
                //= Client Information(step 1)
                username: {
                    required: true
                },
                email: {
                    required: true,
                    email: true
                },
                password: {
                    required: true
                },

                //= Client Information(step 2)
                name: {
                    required: true
                },
                contact: {
                    required: true
                },

                //= Client Information(step 3)
                // Billing Information
                // billing_card_name: {
                //     required: true
                // },
                // billing_card_number: {
                //     required: true,
                //     creditcard: true
                // },
                // billing_card_exp_month: {
                //     required: true
                // },
                // billing_card_exp_year: {
                //     required: true
                // },
                // billing_card_cvv: {
                //     required: true,
                //     minlength: 2,
                //     maxlength: 3
                // },

                //= Confirmation(step 4)
                accept: {
                    required: true
                }
            },

            // Validation messages
            messages: {
                accept: {
                    required: "You must accept the Terms and Conditions agreement!"
                }
            },

            // Display error
            invalidHandler: function(event, validator) {
                KTUtil.scrollTop();

                swal.fire({
                    "title": "",
                    "text": "There are some errors in your submission. Please correct them.",
                    "type": "error",
                    "confirmButtonClass": "btn btn-secondary m-btn m-btn--wide"
                });
            },

            // Submit valid form
            submitHandler: function (form) {

            }
        });
    };

    var initSubmit = function() {
        debugConsole('init submit called');
        // var btn = formEl.find('[data-kwizard-type="action-submit"]');
        var btn = $('[data-ktwizard-type="action-submit"]');
        // var btn = $('.btn-success');
        btn.on('click', function(e) {
            // e.preventDefault();
            if (validator.form()) {
                // See: src\js\framework\base\app.js
                KTApp.progress(btn);
                //KApp.block(formEl);



                var param = {};
                param.userId= $('#kt_form [name=username]').val();
                // param.email= $('#kt_form [name=email]').val();
                // param.password= $('#kt_form [name=password]').val();
                param.userName= $('#kt_form [name=name]').val();
                // param.userTel= $('#kt_form [name=phone]').val();

                procCallAjax("/generator/user","POST",JSON.stringify(param), null, function(data) {
                    if(data.success){

                        KTApp.unprogress(btn);
                                //KApp.unblock(formEl);

                        swal.fire({
                            "title": "",
                            "text": "The application has been successfully submitted!",
                            "type": "success",
                            "confirmButtonClass": "btn btn-secondary"
                        });
                        // location.reload();
                    } else {
                        KTApp.unprogress(btn);
                        swal.fire({
                            "title": "",
                            "text": data.message,
                            "type": "error"
                        });
                    }
                });

                // See: http://malsup.com/jquery/form/#ajaxSubmit
                // formEl.ajaxSubmit({
                //     success: function() {
                //         KApp.unprogress(btn);
                //         //KApp.unblock(formEl);
                //
                //         swal({
                //             "title": "",
                //             "text": "The application has been successfully submitted!",
                //             "type": "success",
                //             "confirmButtonClass": "btn btn-secondary"
                //         });
                //     },
                //     fail:function () {
                //         swal({
                //             "title": "",
                //             "text": "The application has been successfully submitted!",
                //             "type": "success",
                //             "confirmButtonClass": "btn btn-secondary"
                //         });
                //     }
                // });
            }
        });
    };


    return {
        // public functions
        init: function() {
            wizardEl = KTUtil.get('kt_wizard_v4');
            formEl = $('#kt_form');

            initWizard();
            initValidation();
            initSubmit();
        }

    };
}();

jQuery(document).ready(function() {
    KTWizardDemo.init();
});
