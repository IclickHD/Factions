package io.github.iclickhd.factions.logic;

public interface ICacheableLogic<Model> {
	void addOrUpdate(Model model);
	void delete(Model model);
}
