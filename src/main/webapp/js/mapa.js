var liczbaOkregow = mapaFrekwencji.length;
for(var i = 0; i < liczbaOkregow; i++) {
    var dane = mapaFrekwencji[i];
    var okreg = $('#okreg' + dane[0]);
    okreg.tooltip({title: dane[1], container:'body'});
    okreg.popover({title: dane[1], content: dane[2], container:'body'});
}