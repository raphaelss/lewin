var fs = require('fs'),
    hb = require('handlebars');

var outputDir = "build/"
var template = hb.compile(fs.readFileSync("template.html").toString());

function generate(lang) {
  var context = JSON.parse(fs.readFileSync(lang + '.json').toString());
  fs.writeFileSync(outputDir + lang + '.html', template(context));
}

try {
  fs.mkdirSync(outputDir);
} catch (e) {
  if (e.code !== 'EEXIST')
    throw e;
}
generate('english');
