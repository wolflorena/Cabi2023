/**
 * Formats a given time string from HH:mm:ss format to a 12-hour format with AM/PM notation.
 *
 * @param timeStr The time string in "HH:mm:ss" format. If the string is undefined or null,
 *                the function will return an empty string.
 *
 * @return A string representing the formatted time in "hh:mm AM/PM" format. If the input
 *         is null or undefined, returns an empty string.
 */
export function formatTime(timeStr: string | undefined): string {
  if (timeStr) {
    const timeParts = timeStr.split(":");
    const date = new Date();
    date.setHours(
      parseInt(timeParts[0]),
      parseInt(timeParts[1]),
      parseInt(timeParts[2])
    );

    let hours = date.getHours();
    const minutes = date.getMinutes();

    const amPm = hours >= 12 ? "PM" : "AM";

    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'

    const formattedMinutes = minutes < 10 ? `0${minutes}` : minutes;
    return `${hours}:${formattedMinutes} ${amPm}`;
  }
  return "";
}

/**
 * Formats a given date string into a more readable format "DD MMM YYYY". Where MMM are the first 3 letters
 * of the month
 *
 * @param dateStr The date string in a format recognized by the JavaScript Date constructor.
 *                If the string is undefined, null, or invalid, the function will return an empty string.
 * @return A string representing the formatted date in "DD MMM YYYY" format. If the input is null,
 *         undefined, or results in an invalid date, returns an empty string.
 */
export function formatDateForTable(dateStr: string): string {
  const monthsOfYear = [
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "May",
    "Jun",
    "Jul",
    "Aug",
    "Sept",
    "Oct",
    "Nov",
    "Dec",
  ];
  if (dateStr) {
    const date = new Date(dateStr);
    const dayOfMonth = date.getDate();
    const year = date.getFullYear();
    const month = monthsOfYear[date.getMonth()];

    return `${dayOfMonth} ${month} ${year}`;
  }
  return "";
}

/**
 * Formats a date string into a human-readable format "DayOfWeek, DayOfMonth of Month".
 * This function converts a standard date string into a more descriptive format,
 * including the day of the week, the day of the month with an ordinal suffix,
 * and the full name of the month.
 *
 * @param dateStr The date string in a format recognized by the JavaScript Date constructor.
 *                If the string is undefined, null, or invalid, the function will return an empty string.
 * @return A string representing the formatted date as "DayOfWeek, DayOfMonth of Month".
 *         If the input is null, undefined, or results in an invalid date, returns an empty string.
 */
export function formatDate(dateStr: string | undefined): string {
  const daysOfWeek = [
    "Sunday",
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
  ];
  const monthsOfYear = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];
  if (dateStr) {
    const date = new Date(dateStr);
    const dayOfWeek = daysOfWeek[date.getDay()];
    const dayOfMonth = date.getDate();
    const month = monthsOfYear[date.getMonth()];

    const dayWithSuffix = getDayWithSuffix(dayOfMonth);

    return `${dayOfWeek}, ${dayWithSuffix} of ${month}`;
  }
  return "";
}

/**
 * Appends the appropriate ordinal suffix to a numeric day.
 * This function takes a day of the month and returns it as a string
 * with the appropriate ordinal suffix ('st', 'nd', 'rd', 'th').
 *
 * @param day The day of the month as a number.
 * @return A string representing the day with its ordinal suffix.
 */
function getDayWithSuffix(day: number): string {
  if (day % 10 === 1 && day !== 11) {
    return `${day}st`;
  } else if (day % 10 === 2 && day !== 12) {
    return `${day}nd`;
  } else if (day % 10 === 3 && day !== 13) {
    return `${day}rd`;
  } else {
    return `${day}th`;
  }
}
