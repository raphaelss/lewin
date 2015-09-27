var child_process = require('child_process'),
    fs = require('fs'),
    hb = require('handlebars');

var output_dir = "build"
var template_file = "template.html";
var static_dir = "static";
var lang_dir = "langs";
var default_lang = "english";

var template = hb.compile(fs.readFileSync(template_file).toString());

if (!/\/$/.test(output_dir))
  output_dir += '/';
if (!/\/$/.test(lang_dir))
  lang_dir += '/';

function generate(lang_file) {
  var context = JSON.parse(fs.readFileSync(lang_dir + lang_file).toString());
  var lang = lang_file.replace(/\.json$/, "");
  var output_file = lang === default_lang ? "index.html" : lang + ".html";
  fs.writeFileSync(output_dir + output_file, template(context));
}

try {
  fs.mkdirSync(output_dir);
} catch (e) {
  if (e.code !== 'EEXIST')
    throw e;
}

child_process.exec("cp -R " + static_dir + "/* " + output_dir, function (err) {
  if (err)
    console.log(err);
});

fs.readdir(lang_dir, function (err, files) {
  if (err) {
    console.log(err);
    return;
  }
  files.forEach(function (file) {
    generate(file);
  });
});
