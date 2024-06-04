<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
  <head>
    <title>Appointment Notification</title>
  </head>

  <body
    style="
      margin: 0;
      padding: 0;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background-color: #f2f2f2;
    "
  >
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td
          align="center"
          valign="center"
          bgcolor="#333333"
          style="background-color: #43506c; height: 100px"
        >
          <h1 style="color: #e9e9e8">Appointment notification</h1>
        </td>
      </tr>
      <tr
        style="
          background-color: #c8c8ca;
          box-shadow: 0px -10px 10px rgba(0, 0, 0, 0.1);
        "
      >
        <td
          align="left"
          valign="center"
          bgcolor="#c8c8ca"
          style="
            background-color: #c8c8ca;
            padding: 20px;
            box-shadow: 0px -10px 10px rgba(0, 0, 0, 0.1);
          "
        >
          <p style="font-size: 18px; line-height: 1.5">
            Dear <span style="font-weight: bold">${firstName}</span>,
          </p>
          <p style="font-size: 16px; line-height: 1.5">
            We are pleased to inform you that your appointment request has been sent
            at <span style="font-weight: bold">${companyName}</span>. Below are the details of your appointment:
          </p>
          <div
            align="left"
            style="
              background-color: rgba(180, 179, 179, 0.4);
              border-left: 4px solid #ef4b4c;
              padding: 10px;
              margin: 20px 0;
            "
          >
            <p style="font-size: 16px; line-height: 1.5; margin: 5px">
              <strong style="font-size: 18px">Appointment Details:</strong><br />
              <b>Date:</b> ${appointmentDate}<br />
              <b>Time:</b> ${appointmentTime}<br />
              <b>Doctor:</b> ${doctorName}<br />
              <b>Treatment:</b> ${treatment}
            </p>
          </div>
          <p style="font-size: 16px; line-height: 1.5">
            If you have any questions or need to reschedule, feel free to contact
            our support team.
          </p>
          <p style="font-size: 16px; line-height: 1.5">
            Best regards,<br />
            <span style="font-style: italic"><b>${companyName} Team</b></span>
          </p>
        </td>
      </tr>
    </table>
  </body>
</html>
