/*
$(document).ready(function () {
    // add row
    $("#addRow").click(function () {

        var html = '';
        html += '<div id="inputFormRow">';
        html += '<div class="input-group mb-3">';
        html += '<input type="text" name="instructions" class="form-control m-input" placeholder="Enter title" autocomplete="off">';
        html += '<div class="input-group-append">';
        html += '<button id="removeRow" type="button" class="btn btn-danger">Remove</button>';
        html += '</div>';
        html += '</div>';

        $('#newRow').append(html);
    });

    // remove row
    $(document).on('click', '#removeRow', function () {
        $(this).closest('#inputFormRow').remove();

    });

    $("#addRowIngr").click(function () {

        var modelAttributeValue = $.escapeSelector([[${measurements}]]) ;
        console.log(modelAttributeValue.length);
        var html = '';
        html += '<div id="inputFormRow">';
        html += '<div class="input-group mb-3">';
        html += '<input type="number" name="ingredientQuantities" class="form-control m-input" min="0">';
        html += '<select name="ingredientMeasurements" class="form-control">';

        var i;
        for (i = 0; i < modelAttributeValue.length; i++) {
            var itemM = modelAttributeValue[i];
            console.log(itemM);
            var opt = document.createElement("option");
            opt.setAttribute("value", itemM.toString());
            opt.text = itemM.toString();
            console.log(opt.outerHTML);
            html += opt.outerHTML;
        }
        html += '</select>';
        html += '<input type="text" name="ingredientNames" class="form-control m-input" placeholder="Ingredient name" autocomplete="off">';
        html += '<div class="input-group-append">';
        html += '<button id="removeRow" type="button" class="btn btn-danger">Remove</button>';
        html += '</div>';
        html += '</div>';

        $('#newRowIngr').append(html);
    });


    $(document).on("click", ".open-AddBookDialog", function () {
        var recipe = $(this).attr('id');
        var path = '/recipes/delete/' + recipe;
        $(".formDel").attr('action', String(path));
    });

});












*/
