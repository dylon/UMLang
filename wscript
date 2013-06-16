#!/usr/bin/env python2
# encoding: utf-8

APPNAME = 'umlang'
APPVERSION = '0.1.0-alpha'

top = 'src'
out = 'build'

def init_env(ctx):
  try:
    ctx.env
  except:
    setattr(ctx, 'env', {})
  if 'JAVA_PKG' not in ctx.env:
    ctx.env['JAVA_PKG'] = ['com', 'github', 'dylon', 'umlang']

def options(opt):
  opt.recurse('src/antlr')
  opt.recurse('src/java')

def configure(cfg):
  cfg.check_waf_version(mini='1.7.11')
  init_env(cfg)
  cfg.recurse('src/antlr')
  cfg.recurse('src/java')

def generate(gen):
  init_env(gen)
  gen.recurse('src/antlr')

def deploy(dep):
  init_env(dep)
  dep.recurse('src/antlr')

def build(bld):
  init_env(bld)
  bld.recurse('src/java')

