var fs = require('fs'),
    hb = require('handlebars');

var outputDir = "build/"
var template_file = "template.html";
var to_copy = ["style.css"];

var template = hb.compile(fs.readFileSync(template_file).toString());

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

for (var i = 0; i < to_copy.length; ++i) {
  fs.createReadStream(to_copy[i])
    .pipe(fs.createWriteStream(outputDir + to_copy[i]));
}
generate('english');
