$(document).ready(function () {
   $(document).on("click", "a", startProgress);
   $(document).on("submit", "form", startProgress);
   $(document).on("click", ".tool", startProgress);
});
function startProgress() {
    $(".progress").css("display", "block");
    $(".progress_wr").css("display", "block");

}