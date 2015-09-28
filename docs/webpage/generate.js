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
  var lang_links = [];
  var contexts = files.map(function (x) {
    var lang = x.replace(/\.json$/, "");
    var obj = JSON.parse(fs.readFileSync(lang_dir + x).toString());
    obj.output_file = lang === default_lang ? "index.html" : lang + ".html";
    obj.lang_links = lang_links;
    lang_links.push({
      "link": obj.output_file,
      "short": obj.short_lang_name
    });
    return obj;
  });
  contexts.forEach(function (x) {
    fs.writeFileSync(output_dir + x.output_file, template(x));
  });
});
