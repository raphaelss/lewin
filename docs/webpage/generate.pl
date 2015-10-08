#!/usr/bin/perl -I libs/lib/perl5
use strict;
use warnings;
use autodie;
use Mason;
use JSON;
use File::Copy;

my $json = JSON->new();
$json->utf8([1]);

sub get_lang_files {
    my ($dirpath) = @_;
    opendir(my $lang_dir, $dirpath);
    my @lang_files = grep {/\.json$/} readdir($lang_dir);
    closedir($lang_dir);
    return @lang_files;
}

sub read_lang_file {
    my ($lang_dir, $lang) = @_;
    open my $fh, '<', $lang_dir . $lang;
    local $/;
    my $href = $json->decode(<$fh>);
    close $fh;
    $lang =~ s/\.json$//;
    $href->{lang} = $lang;
    return $href;
}

sub setup_links {
    my ($output_dir, $default_lang, $table) = @_;
    $output_dir .= '/' if index($output_dir, '/') ne length($output_dir) - 1;
    for (@$table) {
        my $name = $_->{lang} eq $default_lang? 'index' : $_->{lang};
        $_->{link} = $name . '.html';
        $_->{dest} = $output_dir . $name . '.html';
    }
    for my $lang (@$table) {
        my @link_list;
        for my $other (@$table) {
            unshift(@link_list, {
                link => $lang->{lang} eq $other->{lang} ? '#' : $other->{link},
                short_name => $other->{short_lang_name}
            });
        }
        $lang->{lang_links} = \@link_list;
    }
}

sub read_lang_files {
    my ($lang_dir, $output_dir, $default_lang) = @_;
    $lang_dir .= '/' if index($lang_dir, '/') ne length($lang_dir) - 1;
    my @tables = map {read_lang_file($lang_dir, $_)} get_lang_files($lang_dir);
    setup_links($output_dir, $default_lang, \@tables);
    return @tables;
}

my $default_lang = 'english';
my $langs_path = 'langs';
my $output_dir = 'build';

$output_dir =~ s/\/$//;
if (not -e $output_dir) {
    mkdir($output_dir);
} elsif (not -d $output_dir) {
    die "$output_dir already exists and is not a directory";
}

`cp -R static/* $output_dir`;

my $interp = Mason->new(
    comp_root => '.',
    data_dir => 'tmp'
);

for (read_lang_files($langs_path, $output_dir, $default_lang)) {
    open(my $fh, '>', $_->{dest});
    binmode($fh, ':encoding(UTF-8)');
    print $fh $interp->run('/template', %$_)->output();
    close($fh)
}
