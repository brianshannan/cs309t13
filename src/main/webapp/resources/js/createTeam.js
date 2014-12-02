$( document ).ready(function() {
    $('#createTeamForm').bootstrapValidator({
        message: 'This value is not valid',
        fields: {
            teamName: {
                validators: {
                    notEmpty: {
                        message: 'Your team name is required'
                    },
                    stringLength: {
                        max: 45,
                        message: 'Your team name must be less than 46 characters long'
                    }
                }
            },
            invitedPlayerUsername: {
                validators: {
                    notEmpty: {
                        message: 'An invited player username is required'
                    },
                    stringLength: {
                        max: 45,
                        message: 'Invited usernames must be less than 46 characters long'
                    }
                }
            }
        }
    })
});