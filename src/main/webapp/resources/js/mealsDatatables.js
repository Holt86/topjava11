var ajaxUrl = "ajax/meals/";
var datatableApi;

function updateTable(){
    var form = $("#between");
    $.ajax({
            type: "POST",
            url: ajaxUrl + 'filter',
            data: form.serialize(),
            success: function(data){
                datatableApi.clear().rows.add(data).draw();
            }
        }
    )
}
function save() {
    var form = $("#detailsForm");
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: form.serialize(),
        success: function () {
            $("#editRow").modal("hide");
            updateTable();
            successNoty("Saved");
        }
    });
}
function clearFilter(){
    $("#between").find(":input").val("");
    updateTable();
}

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
});
