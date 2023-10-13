#include "Game.h"
#include <functional>
#include <fstream>
#include <iostream>

Game::Game(const std::string levelsFolderPath, const std::string saveFolderPath)
    :player{{}}, board{}, currentLevel{-1}, connectors{{}},rules{},ruleFactory{board,rules},
      levelsFolderPath{levelsFolderPath}, saveFolderPath{saveFolderPath}
{
    ruleFactory.observeRules(*this);
}

void Game::move(Direction dir){
    if(dir == Direction::S || dir == Direction::E){
        std::reverse(player.begin(),player.end());
    }
    for (Block& playerBlock : player) {
        Position destination = playerBlock.getPosition() + dir;
        if(board.isInsideBoard(destination)){
            Block targetBlock = board.getBlock(destination);
            std::string ruleName = rules.getBlockRuleName(targetBlock.getName(), targetBlock.getType());
            Rule* rule = ruleFactory.getRulePointer(ruleName);
            rule->execute(targetBlock, playerBlock);
            if(rule->getName() == Rule::WIN_IDENTIFIER) return;
        }
    }
     // in case we lost some player blocks in the process :
    fetchPlayer();
}

void Game::fetchAndApplyRules(){
    for(const BlockRef connector : connectors){
        std::optional<RuleChain> rc;

        rc = fetchRule(connector.get().getPosition(), CheckDirection::VERTICAL, connector);
        if(rc.has_value()){
            applyRule(rc.value());
        }

        rc = fetchRule(connector.get().getPosition(), CheckDirection::HORIZONTAL, connector);
        if(rc.has_value()){
            applyRule(rc.value());
        }
    }
}

void Game::applyRule(RuleChain ruleChain){
    if(ruleChain.transformsBlock()){
        board.replaceBlocks(ruleChain.RuleToBlockName(Type::SUBJECT), ruleChain.RuleToBlockName(Type::BEHAVIOR));
    }else{
        std::string subjectName = ruleChain.RuleToBlockName(Type::SUBJECT);
        std::string behaviorName = ruleChain.RuleToBlockName(Type::BEHAVIOR);
        rules.addRule(subjectName, behaviorName);
        if(behaviorName == Rule::PLAYER_IDENTIFIER){
            fetchPlayer();
        }
        Rule* rule = ruleFactory.getRulePointer(behaviorName);
        if(rule->instant){
            rule->execute(ruleChain.subject, ruleChain.behavior);
        }
    }
}

void Game::removeRule(RuleChain ruleChain){
    if(!ruleChain.transformsBlock()){
        std::string behaviorName = ruleChain.RuleToBlockName(Type::BEHAVIOR);
        rules.removeRule(ruleChain.RuleToBlockName(Type::SUBJECT), behaviorName);
        if(behaviorName == Rule::PLAYER_IDENTIFIER){
            fetchPlayer();
        }
    }
}

std::optional<RuleChain> Game::fetchRule(Position rulePos, CheckDirection dir, Block& block){
    Direction readDir = dir == CheckDirection::VERTICAL ? Direction::S : Direction :: E;
    try {
        switch(block.getType()){
        case Type::BEHAVIOR:{
            Block subject = board.getBlock(rulePos-readDir-readDir);
            Block connector = board.getBlock(rulePos-readDir);
            RuleChain rc(subject,connector,block);
            if(rc.isValid()) return rc;
        }
            break;
        case Type::CONNECTOR: {
            Block subject = board.getBlock(rulePos-readDir);
            Block behavior = board.getBlock(rulePos + readDir);
            RuleChain rc(subject,block,behavior);
            if(rc.isValid()) return rc;
        }
            break;
        case Type::SUBJECT: {
            Block connector = board.getBlock(rulePos + readDir);
            Block behavior = board.getBlock(rulePos + readDir + readDir);
            RuleChain rc(block,connector,behavior);
            if(rc.isValid()) return rc;
            connector = board.getBlock(rulePos - readDir);
            behavior = board.getBlock(rulePos - readDir - readDir);
            rc = {block, connector, behavior};
            if(rc.isValid()) return rc;
        }
            break;
        default:
            break;
        }
        return {};
    } catch (...) {
        return {};
    }
}

bool Game::isFinished(){
    return state == GameState::LOST || state == GameState::WON;
}

void Game::startGame(){
    initialise();
}

void Game::saveGame(){
    board.saveBoard(currentLevel, saveFolderPath);
}

bool Game::loadNextLevel(){
    try {
        board.loadLevel(levelsFolderPath + "/level_"+std::to_string(++currentLevel)+".txt");
        initialise();
        return true;
    } catch (...) {
        return false;
    }
}

bool Game::hasSave()
{
    std::ifstream file(saveFolderPath+"/save.txt");
    file.seekg(0,std::ios::end);
    return file.tellg();
}

void Game::initialise(){
    rules.clear();
    connectors = board.getBlocks("is", Type::CONNECTOR);
    std::vector<BlockRef> fromConnectors = board.getBlocks("from", Type::CONNECTOR);
    connectors.insert(connectors.end(), fromConnectors.begin(), fromConnectors.end());
    fetchAndApplyRules();
    fetchPlayer();
    state = GameState::PLAYING;
}

void Game::loadSave(){
    if(hasSave()){
        std::optional<int> r = board.loadLevel(saveFolderPath + "/save.txt", true);
        if(r.has_value()) {
            currentLevel = r.value();
            initialise();
        }else{
            loadNextLevel();
        }
    }
    else
    {
        loadNextLevel();
    }
}

void Game::fetchPlayer(){
    player.clear();
    for (std::string& playerBlock : rules.getPlayer()) {
        std::vector<BlockRef> blocks = board.getBlocks(playerBlock, Type::BLOCK);
        for(BlockRef& block : blocks){
            player.push_back(block);
        }
    }
}

void Game::updateWin(){
    if(!loadNextLevel()){
        state = GameState::WON;
    }else {
        startGame();
    }
}

void Game::updateDeath(){
    fetchPlayer();
    if(player.size() == 0){
        state = GameState::LOST;
    }
}

void Game::updateRuleChange(Position oldPos, Position newPos, Block& ruleBlock) {
    std::optional<RuleChain> ruleChain;
    /*
     * Checking for eventual rule breaks
     */
    ruleChain = fetchRule(oldPos, CheckDirection::HORIZONTAL, ruleBlock);
    if(ruleChain.has_value()){
        removeRule(ruleChain.value());
    }
    ruleChain = fetchRule(oldPos, CheckDirection::VERTICAL, ruleBlock);
    if(ruleChain.has_value()){
        removeRule(ruleChain.value());
    }

    /*
     * Checking for eventual rule formations
     */
    ruleChain = fetchRule(newPos, CheckDirection::HORIZONTAL, ruleBlock);
    if(ruleChain.has_value()){
        applyRule(ruleChain.value());
    }
    ruleChain = fetchRule(newPos, CheckDirection::VERTICAL, ruleBlock);
    if(ruleChain.has_value()){
        applyRule(ruleChain.value());
    }
}

void Game::updatePlay(){
    startGame();
}

void Game::updateStartSave(){
    loadSave();
}

void Game::updateStartZero(){
    loadNextLevel();
}

GameState Game::getState() const{
    return state;
}

Board& Game::getBoard(){
    return board;
}

std::vector<BlockRef> Game::getPlayerBlocks() const{
    return player;
}

std::vector<BlockRef> Game::getConnectors() const{
    return connectors;
}

void Game::restartLevel(){
    currentLevel--;
    loadNextLevel();
    startGame();
}

void Game::restartGame(bool consoleRestart)
{
    currentLevel = consoleRestart ? -1 : -2;
    loadNextLevel();
    startGame();
}

void Game::guiStart()
{
    currentLevel = -2;
}

void Game::consoleStart()
{
    currentLevel = -1;
}
