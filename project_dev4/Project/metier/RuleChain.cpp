#include "RuleChain.h"

RuleChain::RuleChain(Block subject, Block connector, Block behavior)
    :subject{subject},connector{connector},behavior{behavior}
{
    if(isValid() && transformsBlock() && behavior.getPosition() < subject.getPosition() ){
        Block temp = behavior;
        this->behavior = subject;
        this->subject = temp;
    }
}

bool RuleChain::isValid(){
    return subject.getType() == Type::SUBJECT
            && connector.getType() == Type::CONNECTOR
            && (behavior.getType() == Type::BEHAVIOR
                || behavior.getType() == Type::SUBJECT);
}

bool RuleChain::transformsBlock(){
    return behavior.getType() == Type::SUBJECT;
}

std::string RuleChain::RuleToBlockName(Type type){
    switch(type){
    case Type::BEHAVIOR: {
        return transformsBlock() ? behavior.getName().substr(5) : behavior.getName();
    }
    case Type::SUBJECT: {
        if (subject.getName().find("text_") != std::string::npos) {
            return subject.getName().substr(5);
        }
        return subject.getName();
    }
    case Type::CONNECTOR: return connector.getName();
    default: throw std::invalid_argument("RuleChain does not contain a BLOCK type block!");
    }
}
