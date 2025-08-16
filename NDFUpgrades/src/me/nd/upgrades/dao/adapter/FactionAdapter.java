package me.nd.upgrades.dao.adapter;

import me.nd.upgrades.database.SQLResultAdapter;
import me.nd.upgrades.database.SimpleResultSet;
import me.nd.upgrades.model.FactionModel;

public class FactionAdapter implements SQLResultAdapter<FactionModel> {
    @Override
    public FactionModel adaptResult(SimpleResultSet resultSet) {
        String tag = (String)resultSet.get("tag");
        Integer poder = (Integer)resultSet.get("poder");
        Integer membros = (Integer)resultSet.get("membros");
        Integer mobs = (Integer)resultSet.get("mobs");
        Integer moedas = (Integer)resultSet.get("moedas");
        Integer voar = (Integer)resultSet.get("voar");
        Integer plantacao = (Integer)resultSet.get("plantacao");
        Integer blocos = (Integer)resultSet.get("blocos");
        Integer attack = (Integer)resultSet.get("attack");
        Integer defesas = (Integer)resultSet.get("defesas");
        Integer ally = (Integer)resultSet.get("ally");
        return FactionModel.builder().tag(tag).poder(poder).membros(membros).mobs(mobs).moedas(moedas).voar(voar).plantacao(plantacao).blocos(blocos).attack(attack).defesas(defesas).ally(ally).build();
    }
}
