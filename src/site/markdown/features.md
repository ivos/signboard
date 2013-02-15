Features
========

User
----

1. __Register__

	As a prospect user I can register with the system to become its user.
Registration data: email (becomes user system ID and login name),
password and password confirmation, first name, last name, phone, skype.
A newly registered user is active.
The first user to register becomes system administrator, 
any subsequent user to register becomes system user.
System records registration date.

1. __Log in__

	As an active user I can log into the system to start using it.
Login data: email, password.
System records last login time.

1. __Log out__

	As a logged-in user I can log out to stop using the system.

1. __My dashboard__

	As a user I can see my dashboard page after I log in 
to get an overview of current status of my assets within the system.

	*__User overview__*

	The page shows my first name, last name, e-mail, phone, skype
and a link to edit my profile.

	*__My projects__*

	If I am a member of a project, 
the section lists the projects I am a member of.
The list shows name attribute of project.

	If I am not a member of any project, 
the section states so.

	The section shows links to to find an existing project
and create a new project.

1. __Edit my user profile__

	As a user I can edit my user profile and change 
first name, last name, phone, skype.
I can also change my password separately from the above properties.

1. __Find user__

	As a system administrator I can find a user by 
last name, first name, e-mail, phone, status, date registered,
date of last login.
I can view last name, first name, e-mail, phone, status
attributes in the search table.
I can sort table alphabetically (last name, first name, id),
by recent login, by recent registration.

1. __View user__

	As a system administrator I can view a user's detail.
I can view last name, first name, e-mail, phone, skype, status, 
system roles, registration date, last login time
attributes.

1. __Edit user__

	As a system administrator I can edit a user's system roles attribute.
I cannot remove system administrator role from my own user.

1. __Disable and activate user__

	As a system administrator I can disable an active user 
and activate a disabled user. I cannot disable my own user.

Project
-------

1. __Create project__

	As a system user I can create a project.
New project attributes: code (unique), name, description.
System records date created.
I become active project administrator and project user 
of the newly created project.

1. __Find a project__

	As a system user I can find a project by name, code.
I can view name, code attributes in the search table.

1. __View project__

	As a system user I can view a project's detail.

	As non active project member, I can view code, name
attributes.

	As active project member, I can view code, name, description, date created
attributes.

	As project member I can view my project status and my project roles
in the project.

1. __Edit project__

	As an active project administrator I can edit the project's 
name, description attributes.

1. __Join project__

	As a system user I can request to join a project 
I am not a member of yet to become its member.
I am created a pending project user 
(until project administrator confirms my membership request).

1. __Find project member__

	As an active project member I can find any other member by 
last name, first name, e-mail, phone, member status, date registered,
date of last login.
I can view last name, first name, e-mail, phone, member status
attributes in the search table.
I can sort table alphabetically (last name, first name, id),
by recent login, by recent registration.

1. __View project member__

	As an active project member I can view any other member's detail.
I can view last name, first name, e-mail, phone, skype, member status, 
project roles, registration date, last login time
attributes.

1. __Edit project member__

	As an active administrator of the project I can edit a member's roles attribute.
I cannot remove project administrator role from my own member.

1. __Disable and activate project member__

	As an active administrator of the project I can 
disable a non-disabled member and activate a non-active member.
I cannot disable my own member.
