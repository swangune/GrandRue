"use strict";
const METHOD_VERSION="grandrue-preservation-checker/v1";
function unique(a){return new Set(a).size===a.length}
function setEqual(a,b){if(!unique(a)||!unique(b)||a.length!==b.length)return false;const s=new Set(a);return b.every(x=>s.has(x))}
function verifyInventory(baselinePaths,targetPaths,rows){
  const b=rows.filter(r=>r.baseline!==null).map(r=>r.baseline);
  const t=rows.filter(r=>r.target!==null).map(r=>r.target);
  return setEqual(baselinePaths,b)&&setEqual(targetPaths,t)?{result:"PASS"}:{result:"FAIL"};
}
function verifyExactFile(expected,actual){
  if(!actual||actual.completeEvidence===false||actual.contentKnown===false)return {result:"BLOCKED"};
  if(expected.type!==actual.type||expected.mode!==actual.mode)return {result:"FAIL"};
  if(expected.content!==actual.content)return {result:"FAIL"};
  return {result:"PASS"};
}
function verifyProtected(expected,actual,protectedValues){
  const exact=verifyExactFile(expected,actual);if(exact.result!=="PASS")return exact;
  for(const p of protectedValues){if(!actual.content.includes(p))return {result:"FAIL"}}
  return {result:"PASS"};
}
function verifyPathObligation(oldPresent,newPresent,copyAllowed=false){
  return newPresent&&(!oldPresent||copyAllowed)?{result:"PASS"}:{result:"FAIL"};
}
function verifyBridge(nonNamingExecutableDeltas){
  return nonNamingExecutableDeltas.length===0?{result:"PASS"}:{result:"DIFFERENT"};
}
function verifyLiveTarget(declared,observed){return declared===observed?{result:"PASS"}:{result:"STALE"}}
function verifyRegion(fileResults){
  const blocked=fileResults.some(x=>x.result==="BLOCKED"),failed=fileResults.some(x=>x.result==="FAIL");
  return {result:blocked?"BLOCKED":failed?"FAIL":"PASS",fileResults};
}
function runNegativeControls(){
  const C=[];
  const add=(id,expected,actual)=>C.push({id,expected,actual,pass:expected===actual});
  add("positive_exact","PASS",verifyExactFile({type:"blob",mode:"100644",content:"package grandrue;\n"},{type:"blob",mode:"100644",content:"package grandrue;\n"}).result);
  add("inventory_missing_baseline","FAIL",verifyInventory(["a","b"],["x"],[{baseline:"a",target:"x"}]).result);
  add("inventory_omitted_row","FAIL",verifyInventory(["a"],["x","y"],[{baseline:"a",target:"x"}]).result);
  add("inventory_duplicate_target","FAIL",verifyInventory(["a","b"],["x","y"],[{baseline:"a",target:"x"},{baseline:"b",target:"x"}]).result);
  add("truncate_tail","FAIL",verifyExactFile({type:"blob",mode:"100644",content:"alpha\nbeta\ngamma\n"},{type:"blob",mode:"100644",content:"alpha\nbeta\n"}).result);
  add("remove_interior","FAIL",verifyExactFile({type:"blob",mode:"100644",content:"a\nmethod\nz\n"},{type:"blob",mode:"100644",content:"a\nz\n"}).result);
  add("same_length_operator","FAIL",verifyExactFile({type:"blob",mode:"100644",content:"return a+b;"},{type:"blob",mode:"100644",content:"return a-b;"}).result);
  add("protected_identity_change","FAIL",verifyProtected({type:"blob",mode:"100644",content:"package grandrue;\nID=mainstreet-semantic-bundle-v1\n"},{type:"blob",mode:"100644",content:"package grandrue;\nID=grandrue-semantic-bundle-v1\n"},["mainstreet-semantic-bundle-v1"]).result);
  add("old_and_new_owner_both_present","FAIL",verifyPathObligation(true,true,false).result);
  add("mode_only_change","FAIL",verifyExactFile({type:"blob",mode:"100644",content:"x\n"},{type:"blob",mode:"100755",content:"x\n"}).result);
  add("eol_change","FAIL",verifyExactFile({type:"blob",mode:"100644",content:"x\ny\n"},{type:"blob",mode:"100644",content:"x\r\ny\r\n"}).result);
  add("final_newline_change","FAIL",verifyExactFile({type:"blob",mode:"100644",content:"x\n"},{type:"blob",mode:"100644",content:"x"}).result);
  add("missing_consumer_edit","FAIL",verifyExactFile({type:"blob",mode:"100644",content:"import grandrue.Type;\n"},{type:"blob",mode:"100644",content:"import mainstreet.Type;\n"}).result);
  add("truncated_evidence","BLOCKED",verifyExactFile({type:"blob",mode:"100644",content:"x"},{type:"blob",mode:"100644",content:"",completeEvidence:false}).result);
  add("unknown_object_bytes","BLOCKED",verifyExactFile({type:"blob",mode:"100644",content:"x"},{type:"blob",mode:"100644",content:"",contentKnown:false}).result);
  add("hidden_bridge_executable_delta","DIFFERENT",verifyBridge(["operator changed"]).result);
  add("historical_rename_missing_live_target","FAIL",verifyPathObligation(false,false,false).result);
  add("undeclared_live_target","STALE",verifyLiveTarget("D","OTHER").result);
  const rr=verifyRegion([{file:"good",result:"PASS"},{file:"bad",result:"FAIL"}]);
  add("corrupt_file_fails_region","FAIL",rr.result);
  add("independent_good_file_preserved","PASS",rr.fileResults[0].result);
  return C;
}
